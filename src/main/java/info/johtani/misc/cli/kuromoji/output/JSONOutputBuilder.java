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

import java.util.Arrays;
import java.util.StringJoiner;

public class JSONOutputBuilder extends OutputBuilder {

    @Override
    public void output() {
        System.out.println("[");
        StringJoiner sj = new StringJoiner("," + System.lineSeparator());
        tokenList.forEach(
                (TokenInfo token) -> {
                    StringBuilder sb = new StringBuilder("  {");
                    sb.append(System.lineSeparator());
                    sb.append("    \"text\": ");
                    sb.append("\"").append(token.getToken()).append("\",").append(System.lineSeparator());
                    sb.append("    \"detail\": [").append(System.lineSeparator());
                    sb.append(addAllFeatures(sb, token)).append(System.lineSeparator());
                    sb.append("    ]").append(System.lineSeparator());
                    sb.append("  }");
                    sj.add(sb.toString());
                }
        );
        System.out.println(sj.toString());
        System.out.println("]");
    }

    private String addAllFeatures(StringBuilder sb, TokenInfo token) {
        StringJoiner sj = new StringJoiner(","+ System.lineSeparator(), "      \"", "\"");
        Arrays.stream(token.getAllFeatures().split(",")).forEach(sj::add);
        return sj.toString();
    }
}
