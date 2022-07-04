package me.tapeline.quailstudio.utils;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;
import java.util.HashMap;

public class Tokenizer extends AbstractTokenMaker {
    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tt = new TokenMap();
        getWordsToHighlightHash().forEach(tt::put);
        return tt;
    }

    public static HashMap<String, Integer> getWordsToHighlightHash() {
        HashMap<String, Integer> map = new HashMap();
        map.put("func", Token.RESERVED_WORD);
        map.put("function", Token.RESERVED_WORD);
        map.put("method", Token.RESERVED_WORD);
        map.put("staticmethod", Token.RESERVED_WORD);
        map.put("class", Token.RESERVED_WORD);
        map.put("metacontainer", Token.RESERVED_WORD);
        map.put("like", Token.RESERVED_WORD);
        map.put("string", Token.RESERVED_WORD);
        map.put("list", Token.RESERVED_WORD);
        map.put("num", Token.RESERVED_WORD);
        map.put("bool", Token.RESERVED_WORD);
        map.put("null", Token.RESERVED_WORD);
        map.put("if", Token.RESERVED_WORD);
        map.put("elseif", Token.RESERVED_WORD);
        map.put("else", Token.RESERVED_WORD);
        map.put("every", Token.RESERVED_WORD);
        map.put("through", Token.RESERVED_WORD);
        map.put("try", Token.RESERVED_WORD);
        map.put("catch as", Token.RESERVED_WORD);
        map.put("while", Token.RESERVED_WORD);
        map.put("loop", Token.RESERVED_WORD);
        map.put("stop when", Token.RESERVED_WORD);
        map.put("when", Token.RESERVED_WORD);
        map.put("override", Token.RESERVED_WORD);
        map.put("on", Token.RESERVED_WORD);
        map.put("object", Token.RESERVED_WORD);

        map.put("+", Token.OPERATOR);
        map.put("/", Token.OPERATOR);
        map.put("*", Token.OPERATOR);
        map.put("-", Token.OPERATOR);
        map.put("//", Token.OPERATOR);
        map.put("^", Token.OPERATOR);
        map.put("%", Token.OPERATOR);
        map.put(":", Token.OPERATOR);
        map.put("<-", Token.OPERATOR);
        map.put("=", Token.OPERATOR);
        map.put("<", Token.OPERATOR);
        map.put(">", Token.OPERATOR);
        map.put("==", Token.OPERATOR);
        map.put("<=", Token.OPERATOR);
        map.put(">=", Token.OPERATOR);
        map.put("'", Token.OPERATOR);
        map.put("'s", Token.OPERATOR);
        map.put("in", Token.OPERATOR);
        map.put("step", Token.OPERATOR);
        map.put("of", Token.OPERATOR);
        map.put("is type of", Token.OPERATOR);
        map.put("is same type as", Token.OPERATOR);
        map.put("and", Token.OPERATOR);
        map.put("or", Token.OPERATOR);
        map.put("not", Token.OPERATOR);
        map.put("!", Token.OPERATOR);
        map.put("instanceof", Token.OPERATOR);
        map.put("filter", Token.OPERATOR);
        map.put("exists", Token.OPERATOR);
        map.put("notnull", Token.OPERATOR);
        map.put(",", Token.OPERATOR);
        map.put(".", Token.OPERATOR);
        map.put("+=", Token.OPERATOR);
        map.put("-=", Token.OPERATOR);
        map.put("/=", Token.OPERATOR);
        map.put("//=", Token.OPERATOR);
        map.put("^=", Token.OPERATOR);
        map.put("%=", Token.OPERATOR);
        map.put("in power of", Token.OPERATOR);
        map.put("plus", Token.OPERATOR);
        map.put("minus", Token.OPERATOR);
        map.put("divided by", Token.OPERATOR);
        map.put("multiplied by", Token.OPERATOR);
        map.put("is greater than", Token.OPERATOR);
        map.put("is less than", Token.OPERATOR);
        map.put("is greater or equal to", Token.OPERATOR);
        map.put("is less or equal to", Token.RESERVED_WORD_2);
        map.put("should have", Token.RESERVED_WORD_2);
        map.put("should be", Token.RESERVED_WORD_2);
        map.put("should now be", Token.RESERVED_WORD_2);
        map.put("should now be set", Token.RESERVED_WORD_2);
        map.put("should be set", Token.RESERVED_WORD_2);

        map.put("false", Token.LITERAL_BOOLEAN);
        map.put("true", Token.LITERAL_BOOLEAN);

        map.put("do", Token.RESERVED_WORD);
        map.put("does", Token.RESERVED_WORD);
        map.put("has", Token.RESERVED_WORD);
        map.put("with", Token.RESERVED_WORD);
        map.put("end", Token.RESERVED_WORD);

        map.put("throw", Token.RESERVED_WORD_2);
        map.put("assert", Token.RESERVED_WORD_2);
        map.put("use", Token.RESERVED_WORD_2);
        map.put("using", Token.RESERVED_WORD_2);
        map.put("deploy", Token.RESERVED_WORD_2);
        map.put("return", Token.RESERVED_WORD_2);
        map.put("breakpoint", Token.RESERVED_WORD_2);
        map.put("memory", Token.RESERVED_WORD_2);
        map.put("break", Token.RESERVED_WORD_2);
        map.put("continue", Token.RESERVED_WORD_2);
        return map;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        // This assumes all keywords, etc. were parsed as "identifiers."
        if (tokenType == Token.IDENTIFIER) {
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }

    @Override
    public Token getTokenList(Segment text, int startTokenType, int startOffset) {
        resetTokenList();
        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;
        // Token starting offsets are always of the form:
        // 'startOffset + (currentTokenStart-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart'.
        int newStartOffset = startOffset - offset;
        int currentTokenStart = offset;
        int currentTokenType  = startTokenType;
        for (int i=offset; i<end; i++) {
            char c = array[i];
            switch (currentTokenType) {
                case Token.NULL:
                    currentTokenStart = i;   // Starting a new token here.
                    switch (c) {
                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '#':
                            currentTokenType = Token.COMMENT_EOL;
                            break;
                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            currentTokenType = Token.IDENTIFIER;
                            break;
                    }
                    break;
                case Token.WHITESPACE:
                    switch (c) {
                        case ' ':
                        case '\t':
                            break;   // Still whitespace.
                        case '"':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE,
                                    newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE,
                                    newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;
                        default:
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE,
                                    newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            currentTokenType = Token.IDENTIFIER;
                    }
                    break;
                default:
                case Token.IDENTIFIER:
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER,
                                    newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER,
                                    newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        default:
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c=='/' || c=='_') {
                                break;
                            }
                    }
                    break;
                case Token.LITERAL_NUMBER_DECIMAL_INT:
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1,
                                    Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart,i-1,
                                    Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                break;
                            }
                            addToken(text, currentTokenStart,i-1,
                                    Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;
                    }
                    break;
                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
                    currentTokenType = Token.NULL;
                    break;
                case Token.LITERAL_STRING_DOUBLE_QUOTE:
                    if (c=='"') {
                        addToken(text, currentTokenStart,i, Token.LITERAL_STRING_DOUBLE_QUOTE,
                                newStartOffset+currentTokenStart);
                        currentTokenType = Token.NULL;
                    }
                    break;
            }
        }

        switch (currentTokenType) {
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;
            case Token.NULL:
                addNullToken();
                break;
            default:
                addToken(text, currentTokenStart,end-1, currentTokenType,
                        newStartOffset+currentTokenStart);
                addNullToken();
        }
        return firstToken;
    }
}
