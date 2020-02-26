package info.johtani.misc.cli.kuromoji.output;

import info.johtani.misc.cli.kuromoji.Output;

import java.util.ArrayList;
import java.util.List;

public abstract class OutputBuilder {

    List<TokenInfo> tokenList = new ArrayList<>();

    public static class Factory {
        public static OutputBuilder create(Output output) {
            OutputBuilder builder = null;
            switch (output) {
                case wakati:
                    builder = new WakatiOutputBuilder();
                    break;
                case mecab:
                    builder = new MeCabOutputBuilder();
                    break;
            }
            return builder;
        }
    }

    public void addTerm(TokenInfo tokenInfo) {
        tokenList.add(tokenInfo);
    }

    public abstract void output();

}
