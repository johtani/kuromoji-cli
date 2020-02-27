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
