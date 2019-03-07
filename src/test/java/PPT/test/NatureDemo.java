package PPT.test;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;

/**
 * 词性标注
 *
 * @author ansj
 * 
 */
public class NatureDemo {

	public static void main(String[] args) throws IOException {

		Result terms = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!)(*&^%$#@!");
		terms.recognition(new NatureRecognition()) ; //词性标注
		for(int i=0; i<terms.size(); i++){
			System.out.println("文本 : " + terms.get(i).getName() + " 词性 : " + terms.get(i).getNatureStr());
			System.out.println();
		}

	}

}
