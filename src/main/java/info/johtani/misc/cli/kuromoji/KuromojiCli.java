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
import info.johtani.misc.cli.kuromoji.output.Output;
import info.johtani.misc.cli.kuromoji.output.OutputBuilder;
import info.johtani.misc.cli.kuromoji.tokanizer.DictionaryType;
import info.johtani.misc.cli.kuromoji.tokanizer.TokenizerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static com.atilika.kuromoji.TokenizerBase.Mode;
import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

@Command(name = "kuromoji",
        mixinStandardHelpOptions = true,
        version = "0.31.0",
        description = "CLI for Atilika's Kuromoji"
)
public class KuromojiCli implements Callable<Integer> {
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

    @Option(names = {"-v", "--viterbi"},
            defaultValue = "false",
            description = "The output viterbi lattice as DOT format to stdout. And token list is output as stderr"
    )
    boolean outputViterbi = false;

    @Override
    public Integer call() {
        int exitCode = 0;
        try {
            if (inputFile != null && inputFile.isEmpty() == false) {
                FileReader fr = new FileReader(new File(inputFile));
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    tokenize(line, output, dictType, mode);
                }
                br.close();
            }

            Scanner stdin = new Scanner(System.in);
            while (stdin.hasNextLine()) {
                String line = stdin.nextLine();
                tokenize(line, output, dictType, mode);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            exitCode = 1;
        }
        return exitCode;
    }

    void tokenize(String input, Output output, DictionaryType dictType, Mode mode){
        PrintStream outputStream = System.out;
        if (outputViterbi) {
            outputStream = System.err;
        }

        OutputBuilder outputBuilder = OutputBuilder.Factory.create(output, outputStream);
        // TODO support several dictionaries
        TokenizerBase tokenizer = TokenizerFactory.create(dictType, mode);
        List<TokenBase> tokens = (List<TokenBase>) tokenizer.tokenize(input);
        tokens.forEach((token) -> outputBuilder.addTerm(new AtilikaTokenInfo(token.getSurface(), token)));
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

    public static void main(String... args) {
        int exitCode = new CommandLine(new KuromojiCli()).execute(args);
        System.exit(exitCode);
    }
}
