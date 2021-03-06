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

import java.io.PrintStream;

public class MeCabOutputBuilder extends OutputBuilder {

    public MeCabOutputBuilder(PrintStream out) {
        super(out);
    }

    @Override
    public void output() {
        tokenList.forEach(
                (TokenInfo token) -> {
                    //FIXME need change output
                    StringBuilder sb = new StringBuilder(token.getToken() + "\t");
                    sb.append(token.getAllFeatures());
                    super.out.println(sb.toString());
                }
        );
        super.out.println("EOS");
    }


}
