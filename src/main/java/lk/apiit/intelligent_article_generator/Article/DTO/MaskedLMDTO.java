package lk.apiit.intelligent_article_generator.Article.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class MaskedLMDTO implements Serializable {

    private String articleDetails;

    private String generatedText;

    private int noOfSamples;

    private double temperature;

    private int length;

//    private String caption;

//    public MaskedLMDTO(String caption) {
//        this.caption = caption;
//    }
}
