package lk.apiit.intelligent_article_generator.Article.Controller;

import com.itextpdf.text.DocumentException;
import lk.apiit.intelligent_article_generator.Article.DTO.MaskedLMDTO;
import lk.apiit.intelligent_article_generator.Article.Entity.Article;
import lk.apiit.intelligent_article_generator.Article.Service.ArticleService;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/maskedlm", method = RequestMethod.POST)
    public ResponseEntity<?> getMaskedLMResult(@RequestHeader(value = "Authorization") String token,@RequestBody MaskedLMDTO lmdto) throws Exception {
        Utils.checkToken(token);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MaskedLMDTO> entity = new HttpEntity<MaskedLMDTO>(lmdto);

        return restTemplate.exchange("http://localhost:5000/maskedlm", HttpMethod.POST, entity, String.class);
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateArticles(@RequestHeader(value = "Authorization") String token, @RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);
        return ResponseEntity.ok(" ");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveArticle(@RequestHeader(value = "Authorization") String token, @RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);

        articleService.saveAsPDF(dto.getGeneratedText());
        return ResponseEntity.ok("File saved successfully");
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> contentToDownloadArticle(@RequestHeader(value = "Authorization") String token, @PathVariable("id") long id) throws Exception {
        Utils.checkToken(token);
        //returns string content fetched from saved pdf related to the id since the downloading will be handled from the front end
        return ResponseEntity.ok(articleService.getPDFContent(id));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editArticle(@RequestHeader(value = "Authorization") String token, @RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);

        return ResponseEntity.ok(" ");
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArticlesOfUser(@RequestHeader(value = "Authorization") String token) throws Exception {
        Utils.checkToken(token);

        return ResponseEntity.ok(articleService.getArticlesOfUser());
    }
}
