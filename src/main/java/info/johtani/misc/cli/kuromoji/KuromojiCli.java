/*
 * Copyright (c) 2020 johtani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.johtani.misc.cli.kuromoji;

import com.atilika.kuromoji.TokenBase;
import com.atilika.kuromoji.TokenizerBase;
import info.johtani.misc.cli.kuromoji.output.AtilikaTokenInfo;
import info.johtani.misc.cli.kuromoji.output.LuceneTokenInfo;
import info.johtani.misc.cli.kuromoji.output.Output;
import info.johtani.misc.cli.kuromoji.output.OutputBuilder;
import info.johtani.misc.cli.kuromoji.tokenizer.DictionaryType;
import info.johtani.misc.cli.kuromoji.tokenizer.EngineType;
import info.johtani.misc.cli.kuromoji.tokenizer.TokenizerFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttributeImpl;
import org.apache.lucene.analysis.ja.tokenattributes.InflectionAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.InflectionAttributeImpl;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttributeImpl;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.atilika.kuromoji.TokenizerBase.Mode;
import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

@Command(name = "kuromoji",
        mixinStandardHelpOptions = true,
        version = "0.31.0",
        description = "CLI for Atilika's Kuromoji"
)
public class KuromojiCli implements Callable<Integer> {
    private static final String LUCENE_HOTSPOT_VM_OPTIONS_LOGGER = "org.apache.lucene.util.HotspotVMOptions";

    @Parameters(arity = "0..1", description = "The input file path that contains the text for analyzing")
    String inputFile;

    @Option(names = {"-o", "--output"},
            description = "The output format. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    Output output = Output.wakati;

    @Option(names = {"-m", "--mode"},
            description = "The tokenization mode. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    Mode mode = Mode.SEARCH;

    @Option(names = {"-d", "--dictionary"},
            description = "The dictionary of tokenizer. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    DictionaryType dictType = DictionaryType.ipadic;

    @Option(names = {"-e", "--engine"},
            description = "The tokenizer engine. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    EngineType engine = EngineType.atilika;

    @Option(names = {"-v", "--viterbi"},
            defaultValue = "false",
            description = "The output viterbi lattice as DOT format to stdout. And token list is output as stderr"
    )
    boolean outputViterbi = false;

    @Override
    public Integer call() {
        int exitCode = 0;
        try {
            warnUnsupportedOptionsForLucene();
            if (inputFile != null && inputFile.isEmpty() == false) {
                try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(inputFile), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        tokenize(line, output, dictType, mode);
                    }
                }
                return exitCode;
            }

            try (Scanner stdin = new Scanner(System.in, StandardCharsets.UTF_8)) {
                while (stdin.hasNextLine()) {
                    String line = stdin.nextLine();
                    tokenize(line, output, dictType, mode);
                }
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            exitCode = 1;
        }
        return exitCode;
    }

    void tokenize(String input, Output output, DictionaryType dictType, Mode mode) {
        if (engine == EngineType.lucene) {
            tokenizeWithLucene(input, output, mode);
            return;
        }

        tokenizeWithAtilika(input, output, dictType, mode);
    }

    private void tokenizeWithAtilika(String input, Output output, DictionaryType dictType, Mode mode) {
        PrintStream outputStream = System.out;
        if (outputViterbi) {
            outputStream = System.err;
        }

        OutputBuilder outputBuilder = OutputBuilder.Factory.create(output, outputStream);
        // TODO support several dictionaries
        TokenizerBase tokenizer = TokenizerFactory.create(dictType, mode);
        List<?> tokens = tokenizer.tokenize(input);
        for (Object token : tokens) {
            if (token instanceof TokenBase tokenBase) {
                outputBuilder.addTerm(new AtilikaTokenInfo(tokenBase.getSurface(), tokenBase));
            }
        }
        outputBuilder.output();
        if (outputViterbi) {
            try {
                tokenizer.debugTokenize(System.out, input);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void tokenizeWithLucene(String input, Output output, Mode mode) {
        OutputBuilder outputBuilder = OutputBuilder.Factory.create(output, System.out);
        JapaneseTokenizer.Mode luceneMode = JapaneseTokenizer.Mode.valueOf(mode.name());

        try (Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                JapaneseTokenizer tokenizer = new JapaneseTokenizer(createLuceneJapaneseAttributeFactory(), null, false, luceneMode);
                return new TokenStreamComponents(tokenizer);
            }
        };
             TokenStream tokenStream = analyzer.tokenStream("ignored", new StringReader(input))) {
            CharTermAttribute charTerm = tokenStream.getAttribute(CharTermAttribute.class);
            PartOfSpeechAttribute pos = tokenStream.getAttribute(PartOfSpeechAttribute.class);
            ReadingAttribute reading = tokenStream.getAttribute(ReadingAttribute.class);
            InflectionAttribute inflection = tokenStream.getAttribute(InflectionAttribute.class);
            BaseFormAttribute baseForm = tokenStream.getAttribute(BaseFormAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                outputBuilder.addTerm(new LuceneTokenInfo(
                        charTerm.toString(),
                        pos.getPartOfSpeech(),
                        inflection.getInflectionType(),
                        inflection.getInflectionForm(),
                        baseForm.getBaseForm(),
                        reading.getReading(),
                        reading.getPronunciation()
                ));
            }
            tokenStream.end();
            outputBuilder.output();
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to tokenize with Lucene Kuromoji.", ioe);
        }
    }

    private AttributeFactory createLuceneJapaneseAttributeFactory() {
        // Start from Lucene's token factory to avoid reflective lookup for core attributes such as CharTermAttribute.
        // Then pin Japanese attribute implementations explicitly for native-image compatibility.
        AttributeFactory factory = TokenStream.DEFAULT_TOKEN_ATTRIBUTE_FACTORY;
        factory = AttributeFactory.getStaticImplementation(factory, BaseFormAttributeImpl.class);
        factory = AttributeFactory.getStaticImplementation(factory, InflectionAttributeImpl.class);
        factory = AttributeFactory.getStaticImplementation(factory, PartOfSpeechAttributeImpl.class);
        factory = AttributeFactory.getStaticImplementation(factory, ReadingAttributeImpl.class);
        return factory;
    }

    private void warnUnsupportedOptionsForLucene() {
        if (engine != EngineType.lucene) {
            return;
        }

        if (dictType != DictionaryType.ipadic) {
            System.err.println("WARNING: --dictionary is ignored when --engine=lucene. Running with ipadic-equivalent behavior.");
        }
        if (outputViterbi) {
            System.err.println("WARNING: --viterbi is not supported when --engine=lucene. Continuing without DOT output.");
        }
    }

    public static void main(String... args) {
        suppressLuceneHotspotWarning();
        int exitCode = new CommandLine(new KuromojiCli()).execute(args);
        System.exit(exitCode);
    }

    private static void suppressLuceneHotspotWarning() {
        Logger.getLogger(LUCENE_HOTSPOT_VM_OPTIONS_LOGGER).setLevel(Level.OFF);
    }
}
