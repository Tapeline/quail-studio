package me.tapeline.quailstudio.utils;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;

public class FoldingParser implements FoldParser {
    @Override
    public List<Fold> getFolds(RSyntaxTextArea rSyntaxTextArea) {
        List<Fold> folds = new ArrayList<>();
        String code = rSyntaxTextArea.getText();
        int pos = 0;
        try {
            while (pos < rSyntaxTextArea.getText().length()) {
                String fromCurrent = code.substring(pos);
                if (fromCurrent.startsWith("{")) {
                    folds.add(new Fold(FoldType.CODE, rSyntaxTextArea, pos));
                } else if (code.startsWith("}")) {
                    folds.add(new Fold(FoldType.CODE, rSyntaxTextArea, pos));
                }
                pos++;
            }
        } catch (BadLocationException e) {

        }
        return folds;
    }
}
