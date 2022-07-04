package me.tapeline.quailstudio.project;

import me.tapeline.quailstudio.forms.EditorForm;
import me.tapeline.quailstudio.utils.Fonts;
import me.tapeline.quailstudio.utils.Icons;
import me.tapeline.quailstudio.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.search.*;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class CodeEditor extends JPanel implements SearchListener {

    public File file;
    public RSyntaxTextArea area;
    public RTextScrollPane scrollPane;
    private CollapsibleSectionPanel csp;
    private FindDialog findDialog;
    private ReplaceDialog replaceDialog;
    private FindToolBar findToolBar;
    private ReplaceToolBar replaceToolBar;
    public EditorForm form;

    public CodeEditor(EditorForm frame, File f) {
        csp = new CollapsibleSectionPanel();
        add(csp);
        System.out.println(f.getAbsolutePath());
        form = frame;
        file = f;
        setLayout(new GridLayout());
        area = new RSyntaxTextArea();
        area.setBackground(Color.DARK_GRAY);
        area.getSyntaxScheme().getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        area.getSyntaxScheme().getStyle(Token.VARIABLE).foreground = Color.WHITE;
        if (Utils.getExtension(f).equals("q")) {
            area.setSyntaxEditingStyle("text/quail");
            area.getSyntaxScheme().getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
            area.getSyntaxScheme().getStyle(Token.RESERVED_WORD).foreground = new Color(0xCC7832);
            area.getSyntaxScheme().getStyle(Token.RESERVED_WORD_2).foreground = new Color(0xCC7832);
            area.getSyntaxScheme().getStyle(Token.OPERATOR).foreground = new Color(0xCC7832);
            area.getSyntaxScheme().getStyle(Token.SEPARATOR).foreground = new Color(0xCC7832);
            area.getSyntaxScheme().getStyle(Token.LITERAL_BOOLEAN).foreground = new Color(0xCC7832);
            area.getSyntaxScheme().getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground =
                    new Color(0x6897BB);
            area.getSyntaxScheme().getStyle(Token.LITERAL_NUMBER_FLOAT).foreground =
                    new Color(0x6897BB);
            area.getSyntaxScheme().getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground =
                    new Color(0x6A8759);
            area.getSyntaxScheme().getStyle(Token.COMMENT_EOL).foreground = new Color(
                    0x808080);
        } else if (Utils.getExtension(f).equals("yml")) {
            area.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
        }
        area.setHighlightCurrentLine(true);
        area.setCurrentLineHighlightColor(new Color(0x323232));
        area.setCodeFoldingEnabled(true);
        area.setName(f.getAbsolutePath());
        try {
            area.setText(FileUtils.readFileToString(f, "UTF-8"));
        } catch (IOException e) {
            area.setText("CANNOT READ FILE (RECEIVED IOEXCEPTION)");
        }
        area.setFont(Fonts.adapt(Fonts.defaultFont));
        for (Style style : area.getSyntaxScheme().getStyles()) {
            style.font = Fonts.adapt(Fonts.defaultFont);
        }

        CompletionProvider provider = Utils.createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(area);

        scrollPane = new RTextScrollPane(area);
        scrollPane.getGutter().setBookmarkingEnabled(true);
        scrollPane.getGutter().setBookmarkIcon(new ImageIcon(Icons.iconFile));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getGutter().setLineNumberFont(Fonts.adapt(Fonts.defaultFont));
        scrollPane.setFoldIndicatorEnabled(true);
        scrollPane.getGutter().setFoldIcons(new ImageIcon(Icons.iconUnfold),
                new ImageIcon(Icons.iconFold));

        csp.add(scrollPane);

        findDialog = new FindDialog(form, this);
        replaceDialog = new ReplaceDialog(form, this);
        SearchContext context = findDialog.getSearchContext();
        replaceDialog.setSearchContext(context);
        findToolBar = new FindToolBar(this);
        findToolBar.setSearchContext(context);
        replaceToolBar = new ReplaceToolBar(this);
        replaceToolBar.setSearchContext(context);

        int ctrl = getToolkit().getMenuShortcutKeyMask();
        int shift = InputEvent.SHIFT_MASK;
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F, ctrl|shift);
        Action findSearch = csp.addBottomComponent(ks, findToolBar);
        findSearch.putValue(Action.NAME, "Show Find Search Bar");
        area.getPopupMenu().add(new JMenuItem(findSearch));
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_H, ctrl|shift);
        Action replaceSearch = csp.addBottomComponent(ks, replaceToolBar);
        replaceSearch.putValue(Action.NAME, "Show Replace Search Bar");
        area.getPopupMenu().add(new JMenuItem(replaceSearch));

        InputMap iMap = area.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = area.getActionMap();
        iMap.put(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK),
                "Control-F-Find");
        aMap.put("Control-F-Find", findSearch);

        iMap.put(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK),
                "Control-R-Replace");
        aMap.put("Control-R-Replace", replaceSearch);
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(file, area.getText(), "UTF-8");
        } catch (IOException ignored) {}
    }

    public String getSelectedText() {
        return area.getSelectedText();
    }


    /**
     * Creates our Find and Replace dialogs.
     */
    private void initSearchDialogs() {

        findDialog = new FindDialog(form, this);
        replaceDialog = new ReplaceDialog(form, this);

        // This ties the properties of the two dialogs together (match case,
        // regex, etc.).
        SearchContext context = findDialog.getSearchContext();
        replaceDialog.setSearchContext(context);

        // Create tool bars and tie their search contexts together also.
        findToolBar = new FindToolBar(this);
        findToolBar.setSearchContext(context);
        replaceToolBar = new ReplaceToolBar(this);
        replaceToolBar.setSearchContext(context);

    }

    @Override
    public void searchEvent(SearchEvent e) {

        SearchEvent.Type type = e.getType();
        SearchContext context = e.getSearchContext();
        SearchResult result;

        switch (type) {
            default:
            case MARK_ALL:
                result = SearchEngine.markAll(area, context);
                break;
            case FIND:
                result = SearchEngine.find(area, context);
                if (!result.wasFound() || result.isWrapped()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(area);
                }
                break;
            case REPLACE:
                result = SearchEngine.replace(area, context);
                if (!result.wasFound() || result.isWrapped()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(area);
                }
                break;
            case REPLACE_ALL:
                result = SearchEngine.replaceAll(area, context);
                JOptionPane.showMessageDialog(null, result.getCount() +
                        " occurrences replaced.");
                break;
        }

        String text;
        if (result.wasFound()) {
            text = "Text found; occurrences marked: " + result.getMarkedCount();
        }
        else if (type == SearchEvent.Type.MARK_ALL) {
            if (result.getMarkedCount() > 0) {
                text = "Occurrences marked: " + result.getMarkedCount();
            }
            else {
                text = "";
            }
        }
        else {
            text = "Text not found";
        }
    }


    /**
     * Opens the "Go to Line" dialog.
     */
    private class GoToLineAction extends AbstractAction {

        JFrame frame;

        public GoToLineAction(CodeEditor editor) {
            super("Go To Line...");
            frame = editor.form;
            int c = getToolkit().getMenuShortcutKeyMask();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (findDialog.isVisible()) {
                findDialog.setVisible(false);
            }
            if (replaceDialog.isVisible()) {
                replaceDialog.setVisible(false);
            }
            GoToDialog dialog = new GoToDialog(frame);
            dialog.setMaxLineNumberAllowed(area.getLineCount());
            dialog.setVisible(true);
            int line = dialog.getLineNumber();
            if (line>0) {
                try {
                    area.setCaretPosition(area.getLineStartOffset(line-1));
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(area);
                    ble.printStackTrace();
                }
            }
        }

    }

    private class ShowFindDialogAction extends AbstractAction {

        ShowFindDialogAction() {
            super("Find...");
            int c = getToolkit().getMenuShortcutKeyMask();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (replaceDialog.isVisible()) {
                replaceDialog.setVisible(false);
            }
            findDialog.setVisible(true);
        }

    }

    private class ShowReplaceDialogAction extends AbstractAction {

        ShowReplaceDialogAction() {
            super("Replace...");
            int c = getToolkit().getMenuShortcutKeyMask();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (findDialog.isVisible()) {
                findDialog.setVisible(false);
            }
            replaceDialog.setVisible(true);
        }

    }

}
