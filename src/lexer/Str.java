package lexer;

public class Str extends Token {
    public String lexeme = "";

    public Str(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return lexeme;
    }
}
