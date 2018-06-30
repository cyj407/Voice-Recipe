package inputProcessing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class WordSegmenter {

    protected Dictionary dic;

    public WordSegmenter() {
        System.setProperty("mmseg.dic.path", "./src/SegChinese/data");
        dic = Dictionary.getInstance();
    }

    protected Seg getSeg() {
        return new ComplexSeg(dic);
    }

    public ArrayList<String> segWord(String text, String split) {
        ArrayList<String> result = new ArrayList<String>();
        Reader input = new StringReader(text);
        Seg seg = getSeg();
        MMSeg mmSeg = new MMSeg(input, seg);
        Word word = null;
        boolean first = true;
        try {
            while((word = mmSeg.next()) != null) {
                if(!first) {
                    result.add(split);
                }
                String w = word.getString();
                result.add(w);
                first = false;
            }
        }
        catch (IOException e) {
        }
        return result;
    }

}
