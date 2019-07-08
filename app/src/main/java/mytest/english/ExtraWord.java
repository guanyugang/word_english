package mytest.english;

public class ExtraWord {
    private int _id;

    private int Part;

    private int Chapter;

    private String Word;

    private String WordTranslation;

    private String Example;

    private String ExampleTranslation;
    public ExtraWord() {
    }

    public ExtraWord(int id, int Part, int Chapter, String Word, String WordTranslation,
                     String Example, String ExampleTranslation) {
        super();
        this._id = id;
        this.Part = Part;
        this.Chapter = Chapter;
        this.Word = Word;
        this.WordTranslation = WordTranslation;
        this.Example = Example;
        this.ExampleTranslation = ExampleTranslation;
    }

    public ExtraWord(int Part, int Chapter, String Word, String WordTranslation,
                     String Example, String ExampleTranslation) {
        super();
        this.Part = Part;
        this.Chapter = Chapter;
        this.Word = Word;
        this.WordTranslation = WordTranslation;
        this.Example = Example;
        this.ExampleTranslation = ExampleTranslation;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getPart() {
        return Part;
    }

    public void setPart(int Part) {
        this.Part = Part;
    }

    public int getChapter() {
        return Chapter;
    }

    public void setChapter(int Chapter) {
        this.Chapter = Chapter;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String Word) {
        this.Word = Word;
    }

    public String getWordTranslation() {
        return WordTranslation;
    }

    public void setWordTranslation(String WordTranslation) {
        this.WordTranslation = WordTranslation;
    }

    public String getExample() {
        return Example;
    }

    public void setExample(String Example) {
        this.Example = Example;
    }

    public String getExampleTranslation() {
        return ExampleTranslation;
    }

    public void setExampleTranslation(String ExampleTranslation) {
        this.ExampleTranslation = ExampleTranslation;
    }

    @Override
    public String toString() {
        return "Part:" + Part + "   Chapter:" + Chapter + "   Word:" + Word + "   WordTranslation:"
                + WordTranslation + "   Example:" + Example + "   ExampleTranslation:" + ExampleTranslation;
    }
}
