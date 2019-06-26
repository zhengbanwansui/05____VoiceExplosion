package checkSame;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
/** 精确度匹配算法调用流程
    public static void main(String[] args) {
        //分词
        Vector<String> strs1 = participle( "今天你心情怎么样" ) ;
        Vector<String> strs2 = participle( "今天你心情怎么样" ) ;
        //根据分词返回相似度
        double same = 0 ;
        try {
            same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
        System.out.println( "相似度：" + same );
    }
*/
public class CheckTheSame {

    public static Vector<String> participle( String str ) {

        Vector<String> str1 = new Vector<>() ;//对输入进行分词

        try {

            StringReader reader = new StringReader( str );
            IKSegmenter ik = new IKSegmenter(reader,false);//当为true时，分词器进行智能切分
            Lexeme lexeme;

            while( ( lexeme = ik.next() ) != null ) {
                str1.add( lexeme.getLexemeText() );
            }

            if( str1.size() == 0 ) {
                return null ;
            }

            //分词后
            // System.out.println( "str分词后：" + str1 );

        } catch ( IOException e1 ) {
            //System.out.println();
        }
        return str1;
    }

    public String getSemblance(String strone,String strtwo) {
        String semblanceString = "0.0000";
        //分词
        Vector<String> strs1 = participle(strone) ;
        Vector<String> strs2 = participle(strtwo) ;
        //根据分词返回相似度
        double same = 0 ;
        try {
            same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
        } catch (Exception e) {
            //System.out.println( e.getMessage() );
        }
        semblanceString=String.valueOf(same);
        //System.out.println( "相似度：" + same );
        return semblanceString;
    }
}