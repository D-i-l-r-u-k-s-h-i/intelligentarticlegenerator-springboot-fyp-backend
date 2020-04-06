package lk.apiit.intelligent_article_generator.Article.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
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
import lk.apiit.intelligent_article_generator.Auth.Entity.User;
import lk.apiit.intelligent_article_generator.Auth.Repository.UserRepository;
import lk.apiit.intelligent_article_generator.Auth.UserSession;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void saveAsPDF(ArticleDTO articleDTO) throws DocumentException {

        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userRepository.findByUserId(userSession.getId());

        Document document = new Document();
        ByteArrayOutputStream fos=new ByteArrayOutputStream();
        PdfWriter.getInstance(document, fos);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph=new Paragraph(articleDTO.getArticleData(),font);

        document.add(paragraph);
        document.addTitle(String.valueOf(new Date()));
        document.addCreator("Intelligent Article Generator");
        document.addAuthor(userSession.getUsername());
        document.close();

        byte[] pdf=fos.toByteArray();

        Article article=new Article();
        article.setArticleName(articleDTO.getArticleName());
        article.setArticleStatus(new ArticleStatus(2,StatusName.NOT_EDITED));
        article.setArticleFile(pdf);
        article.setDateTime(String.valueOf(new Date()));

        UserArticle userArticle=new UserArticle();
        userArticle.setArticle(article);
        userArticle.setArtucleUser(user);

        articleRepository.save(article);

        userArticleRepository.save(userArticle);
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

    @Transactional
    public void editArticle(ArticleDTO articleDTO) throws IOException {
        Article article;
        if(articleDTO.getId()==0){
            article=new Article();
            article.setDateTime(String.valueOf(new Date()));
            article.setArticleName(String.valueOf(new Date()));
        }
        else {
            article=articleRepository.findByArticleId(articleDTO.getId());
//            article.setLastModifiedDate(new Date());

            //read article data
        }

        //the genrated text will  e of html format. therefore convert to pdf using itext
        File htmlSource = ResourceUtils.getFile("classpath:templates/template.html");
        String htmlString = FileUtils.readFileToString(htmlSource,"UTF-8");

//        String title = "New Page";
        String body = articleDTO.getArticleData();
//        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$body", body);

//        FileUtils.touch(new File("src/main/resources/templates/template2.html"));
//        new FileOutputStream("src/main/resources/templates/template2.html", false);
        File newHtmlFile = new File("src/main/resources/templates/template2.html");

        FileUtils.writeStringToFile(newHtmlFile, htmlString,"UTF-8");

        File pdfDest = new File("src/main/resources/templates/output.pdf");
        // pdfHTML specific code
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(new FileInputStream(newHtmlFile),
                new FileOutputStream(pdfDest), converterProperties);

        //convert file to byte array to save
        byte[] pdfFileData = FileUtils.readFileToByteArray(pdfDest);
        byte[] htmlFileData=FileUtils.readFileToByteArray(newHtmlFile);

        article.setArticleFile(pdfFileData);
        article.setHtmlFile(htmlFileData);

        //change article status to EDITED
        article.setArticleStatus(new ArticleStatus(1,StatusName.EDITED));
        //save article
        articleRepository.save(article);

        if(articleDTO.getId()==0){
            UserArticle userArticle=new UserArticle();
            UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user=userRepository.findByUserId(userSession.getId());
            userArticle.setArtucleUser(user);
            userArticle.setArticle(article);

            userArticleRepository.save(userArticle);
        }
    }

    public String getRichTextFromPDF(long articleId) throws IOException, DocumentException {
        String ret="";
        Article article=articleRepository.findByArticleId(articleId);

        if(article.getHtmlFile()==null){
            ret = getPDFContent(articleId);
        }
        else{
            File htmlSource = new File("src/main/resources/templates/template3.html");

            FileUtils.writeByteArrayToFile(htmlSource, article.getHtmlFile());

            String htmlString = FileUtils.readFileToString(htmlSource,"UTF-8");

            org.jsoup.nodes.Document document = Jsoup.parse(htmlString);

            ret=document.body().html();
        }

        return ret;
    }
}
