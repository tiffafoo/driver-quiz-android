package cs.dawson.dqtiffanytheodore.entities;

/**
 * Question class with the
 * image link and definition
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
    /**
     * Checks if an instance of Question
     * is the same as another.
     * @param {Object} o -- object to compare
     */
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
    /**
     * Override default hashcode
     */
    public int hashCode() {
        int result = getImageLink();
        result = 31 * result + (getDefinition() != null ? getDefinition().hashCode() : 0);
        return result;
    }
}
