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

        HttpEntity<MaskedLMDTO> entity = new HttpEntity<MaskedLMDTO>(lmdto);

        return restTemplate.exchange("http://localhost:5000/maskedlm", HttpMethod.POST, entity, String.class);
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateArticles(@RequestHeader(value = "Authorization") String token, @RequestBody MaskedLMDTO dto) throws Exception {
        Utils.checkToken(token);

        //hardcoding for now
        List<MaskedLMDTO> list=new ArrayList<>();
        list.add(new MaskedLMDTO("Slide 1 jghn kbn jk.bnm bkn/m cvgjhbknm.gchgvjhb.jn/jkvjbk cvkjlbhkjnkm cvhbjn cvghbkjnk cvjhbn cvbjn cvjhbkn vblnjkm vbhjknm cvbhn sxcvbsdfgj dgbhn tdfgh rfghj fghj esdfytugyhuj dfghkjl tydfghj dfghj dfghjkn tdfgyhj fghj rrdfghj rdtfgyh ccgvbhjn hbjkjnk gmhjvhbjn xchgvjhbknk etyfgvbhkjn zsxgcfhvb wretdryfugyiuh zrxtcvbh asdxcvgb zxcvb bjnkm lkfdj utd b gfdjytuyfughli sdrftyguhi 324w54e65r7t dxfcghvjb zxcvghbjn edfcvbhjn esrdftyg etrdytcfvbn lhkgjfhg yuktjydr stdryfgh xcfgvhbj zxcvbh j ytyuryr tdtfvbjn kblvjchxjgh xchvbn gfkhjg"));
        list.add(new MaskedLMDTO("Slide 2"));
        list.add(new MaskedLMDTO("Slide 3"));
        list.add(new MaskedLMDTO("Slide 4"));

        return ResponseEntity.ok(list);
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
}
