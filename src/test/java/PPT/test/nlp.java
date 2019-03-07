package PPT.test;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.util.Collection;

public class nlp {

    public static void main(String[] args) {

        KeyWordComputer kwc = new KeyWordComputer(11);
        String str = "中国共产党是中国工人阶级的先锋队，同时是中国人民和中华民族的先锋队，是中国特色社会主义事业的领导核心，代表中国先进生产力的发展要求，代表中国先进文化的前进方向";
        Collection<Keyword> result = kwc.computeArticleTfidf(str);
        System.out.println(result);
    }

}
