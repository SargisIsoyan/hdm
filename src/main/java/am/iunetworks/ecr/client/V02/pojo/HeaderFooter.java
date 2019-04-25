package am.iunetworks.ecr.client.V02.pojo;

/**
 * Class representing Header or Footer on the Receipt
 * headerAlignment Receipt header and footer alignment HeaderFooter.ALIGNMENT_LEFT, HeaderFooter.ALIGNMENT_CENTER, HeaderFooter.ALIGNMENT_RIGHT
 * Receipt header and footer style HeaderFooter.HEADER_STYLE_NORMAL,HeaderFooter.HEADER_STYLE_BOLD
 * fSize Receipt header and footer font size  HeaderFooter.FONT_SIZE_TINY,HeaderFooter.FONT_SIZE_SMALL,HeaderFooter.FONT_SIZE_MEDIUM,HeaderFooter.FONT_SIZE_LARGE,HeaderFooter.FONT_SIZE_HUGE
 * */
public class HeaderFooter {
    public static final int ALIGNMENT_LEFT = 3;
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_RIGHT = 2;

    public static final int HEADER_STYLE_NORMAL = 0;
    public static final int HEADER_STYLE_BOLD = 1;

    public static final int FONT_SIZE_TINY = 1;
    public static final int FONT_SIZE_SMALL = 2;
    public static final int FONT_SIZE_MEDIUM = 3;
    public static final int FONT_SIZE_LARGE = 4;
    public static final int FONT_SIZE_HUGE = 5;

    private static final int FONT_SIZE_MULTIPLIER = 8;

    int align;
    int bold;
    int fsize;
    String text;

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int isBold() {
        return bold;
    }

    public void setBold(int bold) {
        this.bold = bold;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize * FONT_SIZE_MULTIPLIER;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}