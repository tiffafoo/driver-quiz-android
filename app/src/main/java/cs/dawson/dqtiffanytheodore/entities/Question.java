package cs.dawson.dqtiffanytheodore.entities;

/**
 * Created by sirMerr on 2017-09-29.
 */

public class Question {
    private int imageLink;
    private String definition;
    private String hint;

    /**
     * Default constructor
     * @param imageLink link to image
     * @param definition definition of question
     */
    public Question(int imageLink, String definition, String hint) {
        setImageLink(imageLink);
        setDefinition(definition);
        setHint(hint);
    }

    // Getters and Setters
    public int getImageLink() {
        return imageLink;
    }

    public void setImageLink(int imageLink) {
        this.imageLink = imageLink;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question = (Question) o;

        if (getImageLink() != question.getImageLink()) return false;
        if (getDefinition() != null ? !getDefinition().equals(question.getDefinition()) : question.getDefinition() != null)
            return false;
        return getHint() != null ? getHint().equals(question.getHint()) : question.getHint() == null;

    }

    @Override
    public int hashCode() {
        int result = getImageLink();
        result = 31 * result + (getDefinition() != null ? getDefinition().hashCode() : 0);
        result = 31 * result + (getHint() != null ? getHint().hashCode() : 0);
        return result;
    }
}
