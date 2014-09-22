package org.codehaus.xfire.demo;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
// START SNIPPET: book
public class Book
{
    private String title;
    private String isbn;
    private String author;
  
    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
 
    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}
// END SNIPPET: book
