import java.util.*;
import java.io.*;

public class Bai4 {

    public static Map<String, Integer> corpus = new HashMap<>();
    public static Map<String, Map<String, Integer>> pairCorpus = new HashMap<>();
    public static Map<String, Double> probs = new HashMap<>();
    public static Map<String, Map<String, Double>> conditionalProbs = new HashMap<>();

    public static void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            List<String> lines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim().toLowerCase();
                lines.add(line);
            }
            fileScanner.close();

            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    corpus.put(words[i], corpus.getOrDefault(words[i], 0) + 1);
                    
                    if (i < words.length - 1) {
                        pairCorpus.putIfAbsent(words[i], new HashMap<>());
                        pairCorpus.get(words[i]).put(words[i + 1], pairCorpus.get(words[i]).getOrDefault(words[i + 1], 0) + 1);
                    }
                }
            }
            System.out.println("Đọc file thành công!");
        } catch (FileNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy file!");
            e.printStackTrace();
        }
    }

    public static void constructProbabilities() {
        int totalWords = corpus.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : corpus.entrySet()) {
            probs.put(entry.getKey(), (double) entry.getValue() / totalWords);
        }

        for (Map.Entry<String, Map<String, Integer>> entry : pairCorpus.entrySet()) {
            String w0 = entry.getKey();
            int total = corpus.get(w0);
            conditionalProbs.putIfAbsent(w0, new HashMap<>());
            for (Map.Entry<String, Integer> w1Entry : entry.getValue().entrySet()) {
                conditionalProbs.get(w0).put(w1Entry.getKey(), (double) w1Entry.getValue() / total);
            }
        }
    }

    public static List<String> generateSentence(String w0, int maxWords) {
        List<String> sentence = new ArrayList<>();
        if (!corpus.containsKey(w0)) {
            System.out.println("Từ '" + w0 + "' không có trong tập dữ liệu! Chọn từ phổ biến nhất.");
            w0 = corpus.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }

        sentence.add(w0);

        for (int i = 0; i < maxWords; i++) {
            if (!conditionalProbs.containsKey(w0)) break;

            Map<String, Double> candidates = conditionalProbs.get(w0);
            String nextWord = candidates.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

            if (nextWord == null) break;
            sentence.add(nextWord);
            w0 = nextWord;
        }

        return sentence;
    }

    public static void main(String[] args) {
        String filename = "UIT-ViOCD.txt";
        readFile(filename);
        constructProbabilities();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập một từ bất kỳ: ");
        String inputWord = scanner.nextLine().trim().toLowerCase();

        List<String> result = generateSentence(inputWord, 5);
        System.out.println("Kết quả: " + String.join(" ", result));
        scanner.close();
    }
}
