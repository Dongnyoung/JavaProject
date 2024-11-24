import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class WordVector {
	private Vector<String> v = new Vector<String>();
	private void readFile() {
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("resource/words.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i = 0;
			while ((strLine = br.readLine()) != null) {
				v.add(strLine);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public WordVector() {
		readFile();
	}
	public String get() {
		int index = (int) (Math.random() * v.size());
		return v.get(index);
	}
}