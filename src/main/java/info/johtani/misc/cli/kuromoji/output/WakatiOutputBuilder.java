package info.johtani.misc.cli.kuromoji.output;

public class WakatiOutputBuilder extends OutputBuilder {
    @Override
    public void output() {
        tokenList.forEach(
                (TokenInfo token) -> System.out.print(token.getToken() + " ")
        );
    }
}
