package lexer;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer {

	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();

	void reserve(Word w) {
		words.put(w.lexeme, w);
	}

	public Lexer() {
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));
		reserve(new Word("class", Tag.CLASS));
		reserve(new Word("void", Tag.VOID));
		reserve(new Word("for", Tag.FOR));
		reserve(Word.True);
		reserve(Word.False);
	}

	public void readch() throws IOException {
		peek = (char) System.in.read();

	}

	boolean readch(char c) throws IOException {
		readch();
		if (peek != c) {
			return false;
		}
		peek = ' ';
		return true;
	}

	public Token scan() throws IOException {
		for (;; readch()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n') {
				line += 1;
			} else {
				break;
			}
		}
		switch (peek) {
			case '&':
				if (readch('&'))
					return Word.and;
				else
					return new Token('&');
			case '|':
				if (readch('|'))
					return Word.or;
				else
					return new Token('|');
			case '=':
				if (readch('='))
					return Word.eq;
				else
					return new Token('=');
			case '!':
				if (readch('='))
					return Word.ne;
				else
					return new Token('!');
			case '<':
				if (readch('='))
					return Word.le;
				else
					return new Token('<');
			case '>':
				if (readch('='))
					return Word.ge;
				else
					return new Token('>');
		}

		if (Character.isDigit(peek)) {
			/*int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));*/
			int v = Character.digit(peek, 10);
			readch();
			if (v == 0 && (peek == 'X' || peek == 'x')) {
				v = 0;
				readch();
				do {
					v = 16 * v + Character.digit(peek, 16);
					readch();
				} while (Character.isDigit(peek) || (Character.isLetter(peek) && peek >= 'A' && peek <= 'F'));

				if (peek != '.')
					return new Num(v);

				float x = v;
				float d = 16;
				for (; ; ) {
					readch();
					if (!(Character.isDigit(peek) || (Character.isLetter(peek) && peek >= 'A' && peek <= 'F')))
						break;
					x = x + Character.digit(peek, 16) / d;
					d = d * 16;
				}
				return new Real(x);

			}// Hex

			while (Character.isDigit(peek)) {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			}

			if (peek != '.')
				return new Num(v);
			float x = v;
			float d = 10;
			for (; ; ) {
				readch();
				if (!Character.isDigit(peek))
					break;
				x = x + Character.digit(peek, 10) / d;
				d = d * 10;
			}
			if(peek == 'E' || peek == 'e') {
				readch();
				if(peek == '+') {
					int ee = 0;
					readch();
					while (Character.isDigit(peek)) {
						ee = ee * 10 + Character.digit(peek, 10);
						readch();
					}
					for (int i = 1; i <= ee; i++) {
						x *= 10;
					}
				}
				else if(peek == '-') {
					int ee = 0;
					readch();
					while (Character.isDigit(peek)) {
						ee = ee * 10 + Character.digit(peek, 10);
						readch();
					}
					for (int i = 1; i <= ee; i++) {
						x /= 10;
					}
				}
				else {
					//error
				}
			} // Scientific count
			return new Real(x);
		}

		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek) || peek == '_');
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null)
				return w;
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}

		if (peek == '\"') {
			StringBuffer b = new StringBuffer();
			readch();
			while (peek != '\"' && peek != '\n') {
				b.append(peek);
				readch();
			}
			if (peek == '\n') {
				//error
			} else {
				readch();
				return new Str(b.toString(), Tag.STR);
			}
		}

		if (peek == '/') {
			readch();
			if (peek == '/') {
				while (peek != '\n') {
					readch();
				}
				return new Not(Tag.Not);
				//readch();
			}

			else if (peek == '*') {
				for (;; readch()) {
					if(peek == '*') {
						readch();
						if(peek == '/') {
							readch();
							break;
						}
					}
				}
			}
		}

		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}

	public void out() {
		System.out.println(words.size());

	}

	public char getPeek() {
		return peek;
	}

	public void setPeek(char peek) {
		this.peek = peek;
	}
}