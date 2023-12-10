package lk.apiit.intelligent_article_generator.Article.Controller;

import com.itextpdf.text.DocumentException;
import lk.apiit.intelligent_article_generator.Article.DTO.ArticleDTO;
import lk.apiit.intelligent_article_generator.Article.DTO.MaskedLMDTO;
import lk.apiit.intelligent_article_generator.Article.Entity.Article;
import lk.apiit.intelligent_article_generator.Article.Service.ArticleService;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/combined", method = RequestMethod.POST)
    public ResponseEntity<?> getCombinedGeneratedArticles(@RequestHeader(value = "Authorization") String token,@RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);

        if(dto.getLength()==0){
            dto.setLength(50);
        }

        if(dto.getNoOfSamples()==0){
            dto.setNoOfSamples(1);
        }

        if(dto.getTemperature()==0.0){
            dto.setTemperature(1.2);
        }

        HttpEntity<MaskedLMDTO> entity = new HttpEntity<MaskedLMDTO>(dto);

        return restTemplate.exchange("http://13.82.137.168:5000/combined", HttpMethod.POST, entity, String.class);
//        TimeUnit.SECONDS.sleep(20);
//        return ResponseEntity.ok("{\"generated_text\":[\"mock data hdgfgjhj jhjfgjh hjgjhk hcjhvkb ghcjvjb ytdfyjgk zxcvb hvb trydtfygk tfjg\"]}");
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResponseEntity<?> cancelRequest(@RequestHeader(value = "Authorization") String token) throws Exception {
        Utils.checkToken(token);

        HttpEntity<Object> entity = new HttpEntity<Object>(null);

        return restTemplate.exchange("http://13.82.137.168:5000/cancel", HttpMethod.POST, entity, String.class);
    }


    @RequestMapping(value = "/maskedlm", method = RequestMethod.POST)
    public ResponseEntity<?> getMaskedLMResult(@RequestHeader(value = "Authorization") String token,@RequestBody MaskedLMDTO lmdto) throws Exception {
        Utils.checkToken(token);

        HttpEntity<MaskedLMDTO> entity = new HttpEntity<MaskedLMDTO>(lmdto);

        return restTemplate.exchange("http://52.149.149.23:5000/maskedlm", HttpMethod.POST, entity, String.class);
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateArticles(@RequestHeader(value = "Authorization") String token, @RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);

        if(dto.getLength()==0){
            dto.setLength(50);
        }

        if(dto.getNoOfSamples()==0){
            dto.setNoOfSamples(1);
        }

        if(dto.getTemperature()==0.0){
            dto.setTemperature(0.8);
        }

        HttpEntity<MaskedLMDTO> entity = new HttpEntity<MaskedLMDTO>(dto);

        return restTemplate.exchange("http://52.149.149.23:5000/generate", HttpMethod.POST, entity, String.class);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveArticle(@RequestHeader(value = "Authorization") String token, @RequestBody ArticleDTO dto) throws Exception {
        Utils.checkToken(token);

        articleService.saveAsPDF(dto);
        return ResponseEntity.ok("File saved successfully");
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> contentToDownloadArticle(@RequestHeader(value = "Authorization") String token, @PathVariable("id") long id) throws Exception {
        Utils.checkToken(token);
        //returns string content fetched from saved pdf related to the id since the downloading will be handled from the front end
        return ResponseEntity.ok(articleService.getPDFContent(id));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editArticle(@RequestHeader(value = "Authorization") String token, @RequestBody ArticleDTO dto) throws Exception {
        Utils.checkToken(token);

        articleService.editArticle(dto);
        return ResponseEntity.ok("File edited Successfully");
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArticlesOfUser(@RequestHeader(value = "Authorization") String token) throws Exception {
        Utils.checkToken(token);

        return ResponseEntity.ok(articleService.getArticlesOfUser());
    }

    @RequestMapping(value = "/richtext/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRichTextFromPDF(@RequestHeader(value = "Authorization") String token,@PathVariable("id") long id) throws Exception {
        Utils.checkToken(token);

        return ResponseEntity.ok(articleService.getRichTextFromPDF(id));
    }

    @RequestMapping(value = "/updatename", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateArticleName(@RequestHeader(value = "Authorization") String token, @RequestBody ArticleDTO dto) throws Exception {
        Utils.checkToken(token);

        articleService.updateArticleName(dto);
        return ResponseEntity.ok("Article Name updated successfully");
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteItem(@RequestHeader(value = "Authorization") String token,@PathVariable(name = "id") long id) throws Exception {
        Utils.checkToken(token);

        articleService.deleteArticle(id);
        return ResponseEntity.ok("Article was removed successfully");
    }

    @RequestMapping(value = "/search/{value}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchCraftItems(@RequestHeader(value = "Authorization") String token,@PathVariable(name = "value") String value) throws Exception {
        Utils.checkToken(token);
        return ResponseEntity.ok(articleService.searchArticle(value));
    }
}
