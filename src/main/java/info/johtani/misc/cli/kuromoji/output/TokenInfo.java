package info.johtani.misc.cli.kuromoji.output;

public class TokenInfo {
    String token;
    String pos;
    String reading;
    String pronunciation;
    String baseForm;
    String inflectionType;
    String inflectionForm;

    public TokenInfo(String token, String pos, String reading, String pronunciation, String baseForm,
                     String inflectionType, String inflectionForm) {
        this.token = token;
        this.pos = pos;
        this.reading = reading;
        this.pronunciation = pronunciation;
        this.baseForm = baseForm;
        this.inflectionType = inflectionType;
        this.inflectionForm = inflectionForm;
    }

    public String getToken() {
        return token;
    }

    public String getPos() {
        return pos;
    }

    public String getReading() {
        return reading;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getBaseForm() {
        return baseForm;
    }

    public String getInflectionType() {
        return inflectionType;
    }

    public String getInflectionForm() {
        return inflectionForm;
    }
}
