package lk.apiit.intelligent_article_generator.Article.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lk.apiit.intelligent_article_generator.Article.DTO.ArticleDTO;
import lk.apiit.intelligent_article_generator.Article.Entity.Article;
import lk.apiit.intelligent_article_generator.Article.Entity.ArticleStatus;
import lk.apiit.intelligent_article_generator.Article.Entity.StatusName;
import lk.apiit.intelligent_article_generator.Article.Entity.UserArticle;
import lk.apiit.intelligent_article_generator.Article.Repository.ArticleRepository;
import lk.apiit.intelligent_article_generator.Article.Repository.UserArticleRepository;
import lk.apiit.intelligent_article_generator.Auth.UserSession;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserArticleRepository userArticleRepository;

    public void saveAsPDF(String text) throws DocumentException {

        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Document document = new Document();
        ByteArrayOutputStream fos=new ByteArrayOutputStream();
        PdfWriter.getInstance(document, fos);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph=new Paragraph(text,font);

        document.add(paragraph);
        document.addTitle(String.valueOf(new Date()));
        document.addCreator("Intelligent Article Generator");
        document.addAuthor(userSession.getUsername());
        document.close();

        byte[] pdf=fos.toByteArray();

        Article article=new Article();
        article.setArticleStatus(new ArticleStatus(2,StatusName.NOT_EDITED));
        article.setArticleFile(pdf);
        article.setDateTime(String.valueOf(new Date()));

        articleRepository.save(article);
    }

    public String getPDFContent(long article_id) throws DocumentException {

        Article article=articleRepository.findByArticleId(article_id);

        PdfReader reader;
        String textFromPage="";

        try {

            reader = new PdfReader(article.getArticleFile());

            int noOfPages = reader.getNumberOfPages();

            for(int i = 1; i <= noOfPages; i++) {
                // Extract content of each page
                textFromPage = PdfTextExtractor.getTextFromPage(reader, i);

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textFromPage;
    }

    public List<ArticleDTO> getArticlesOfUser(){

        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserArticle> userArticleList=userArticleRepository.getAllByArtucleUser_UserId(userSession.getId());

        List<Article> articleList=userArticleList.stream().map(UserArticle::getArticle).collect(Collectors.toList());

        return Utils.mapAll(articleList,ArticleDTO.class);
    }
}
