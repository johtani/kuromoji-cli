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

import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;


public class OutputBuilderTest {

    @Test
    public void createWakati() {
        Object obj = OutputBuilder.Factory.create(Output.wakati, System.out);
        assertThat(obj, instanceOf(WakatiOutputBuilder.class));
    }

    @Test
    public void createMeCab() {
        Object obj = OutputBuilder.Factory.create(Output.mecab, System.out);
        assertThat(obj, instanceOf(MeCabOutputBuilder.class));
    }

    @Test
    public void createJson() {
        Object obj = OutputBuilder.Factory.create(Output.json, System.out);
        assertThat(obj, instanceOf(JSONOutputBuilder.class));
    }
}