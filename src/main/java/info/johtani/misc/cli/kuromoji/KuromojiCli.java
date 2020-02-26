package info.johtani.misc.cli.kuromoji;

import info.johtani.misc.cli.kuromoji.output.OutputBuilder;
import info.johtani.misc.cli.kuromoji.output.TokenInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.InflectionAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Callable;

import static org.apache.lucene.analysis.ja.JapaneseTokenizer.Mode;
import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

@Command(name = "kuromoji",
        mixinStandardHelpOptions = true,
        version = "0.10.0",
        description = "CLI for Lucene Kuromoji"
)
public class KuromojiCli implements Callable<Integer> {
    @Parameters(index = "0", description = "The input file path that contains the text for analyzing")
    String input;

    @Option(names = {"-o", "--output"},
            description = "The output format. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    Output output = Output.wakati;

    @Option(names = {"-m", "--mode"},
            description = "The tokenization mode. ${COMPLETION-CANDIDATES} can be specified. Default is ${DEFAULT-VALUE}"
    )
    Mode mode = Mode.SEARCH;

    @Override
    public Integer call() {
        int exitCode = 0;

        OutputBuilder outputBuilder = OutputBuilder.Factory.create(output);

        final Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false, mode);
                return new TokenStreamComponents(tokenizer, tokenizer);
            }
        };

        TokenStream ts = analyzer.tokenStream("ignored", new StringReader(input));
        CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
        PartOfSpeechAttribute pos = ts.getAttribute(PartOfSpeechAttribute.class);
        ReadingAttribute reading = ts.getAttribute(ReadingAttribute.class);
        InflectionAttribute inflection = ts.getAttribute(InflectionAttribute.class);
        BaseFormAttribute baseForm = ts.getAttribute(BaseFormAttribute.class);
        try {
            ts.reset();
            while (ts.incrementToken()) {
                outputBuilder.addTerm(new TokenInfo(
                        cta.toString(),
                        pos.getPartOfSpeech(),
                        reading.getReading(),
                        reading.getPronunciation(),
                        baseForm.getBaseForm(),
                        inflection.getInflectionType(),
                        inflection.getInflectionForm()
                ));
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            exitCode = 1;
        }

        outputBuilder.output();
        System.out.println("");

        return exitCode;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new KuromojiCli()).execute(args);
        System.exit(exitCode);
    }
}
