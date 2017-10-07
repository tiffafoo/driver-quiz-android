package cs.dawson.dqtiffanytheodore.entities;

/**
 * Created by sirMerr on 2017-09-29.
 */

public class Question {
    private int imageLink;
    private String definition;

    /**
     * Default constructor
     * @param imageLink link to image
     * @param definition definition of question
     */
    public Question(int imageLink, String definition) {
        setImageLink(imageLink);
        setDefinition(definition);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question = (Question) o;

        if (getImageLink() != question.getImageLink()) return false;
        if (getDefinition() != null ? !getDefinition().equals(question.getDefinition()) : question.getDefinition() != null)
            return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = getImageLink();
        result = 31 * result + (getDefinition() != null ? getDefinition().hashCode() : 0);
        return result;
    }
}
