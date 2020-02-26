package info.johtani.misc.cli.kuromoji.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeCabOutputBuilder extends OutputBuilder {

    static int POS_LENGTH = 4;

    @Override
    public void output() {
        tokenList.forEach(
                (TokenInfo token) -> {
                    //FIXME need change output
                    StringBuilder sb = new StringBuilder(token.getToken() + "\t");
                    List<String> posInfo = new ArrayList<>(Arrays.asList(token.getPos().split("-")));
                    if (posInfo.size() < 4) {
                        for (int i = posInfo.size(); i < POS_LENGTH; i++) {
                            posInfo.add("*");
                        }
                    }
                    if (token.getInflectionType() == null) {
                        posInfo.add("*");
                    } else {
                        posInfo.add(token.getInflectionType());
                    }
                    if (token.getInflectionForm() == null) {
                        posInfo.add("*");
                    } else {
                        posInfo.add(token.getInflectionForm());
                    }
                    if (token.getBaseForm() == null) {
                        posInfo.add(token.getToken());
                    } else {
                        posInfo.add(token.getBaseForm());
                    }
                    if (token.getReading() == null) {
                        posInfo.add("*");
                    } else {
                        posInfo.add(token.getReading());
                    }
                    if (token.getPronunciation() == null) {
                        posInfo.add("*");
                    } else {
                        posInfo.add(token.getPronunciation());
                    }
                    sb.append(String.join(",", posInfo));
                    System.out.println(sb.toString());
                }
        );
    }


}
