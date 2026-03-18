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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONOutputBuilderTest extends AbstractOutputBuilderTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static JSONOutputBuilder createInstance() {
        return new JSONOutputBuilder(System.out);
    }

    @Test
    public void output() throws Exception {
        OutputBuilder builder = createInstance();
        TokenInfo token = defaultTokenInfo();
        builder.addTerm(token);
        builder.addTerm(token);
        builder.output();

        List<Map<String, Object>> actual = parseOutput(outContent.toString());
        assertEquals(2, actual.size());
        assertEquals("test", actual.get(0).get("text"));
        assertEquals(List.of("all", "features"), actual.get(0).get("detail"));
        assertEquals("test", actual.get(1).get("text"));
        assertEquals(List.of("all", "features"), actual.get(1).get("detail"));
    }

    @Test
    public void outputShouldEscapeSpecialCharacters() throws Exception {
        OutputBuilder builder = createInstance();
        builder.addTerm(new DummyTokenInfoWithFeatures("a\"b\\c\nd\t", "f\"1,f\\2,f\n3,f\t4"));
        builder.output();

        List<Map<String, Object>> actual = parseOutput(outContent.toString());
        assertEquals(1, actual.size());
        assertEquals("a\"b\\c\nd\t", actual.get(0).get("text"));
        assertEquals(List.of("f\"1", "f\\2", "f\n3", "f\t4"), actual.get(0).get("detail"));
    }

    private List<Map<String, Object>> parseOutput(String output) throws Exception {
        return OBJECT_MAPPER.readValue(output, new TypeReference<>() {});
    }
}

class DummyTokenInfoWithFeatures extends TokenInfo {

    private final String features;

    public DummyTokenInfoWithFeatures(String token, String features) {
        super(token);
        this.features = features;
    }

    @Override
    public String getAllFeatures() {
        return features;
    }
}
