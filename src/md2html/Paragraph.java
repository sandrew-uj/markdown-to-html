package md2html;

public class Paragraph {        //class for paragraph parsing
    private final String markup;
    private final int level;

    public Paragraph(String markup, int level) {
        this.markup = markup;
        this.level = level;
    }

    String toHtml() {
        var text = new Text(markup);
        var sb = new StringBuilder();
        String tag = level > 0 ? "h" + level : "p";     //is it h or p with level
        sb.append("<").append(tag).append(">").append(text.toHtml()).append("</").append(tag).append(">");
        return sb.toString();
    }
}
