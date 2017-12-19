import java.util.Scanner;

public class Solution
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] scores = new int[n];
        for (int scores_i = 0; scores_i < n; scores_i++)
        {
            scores[scores_i] = in.nextInt();
        }
        int[] rangs = new int[n];
        int currentRang = 1;
        rangs[0] = currentRang;
        for (int i = 1; i < rangs.length; i++)
        {
            int score = scores[i - 1];
            int currentScore = scores[i];
            if (score != currentScore)
                currentRang++;
            rangs[i] = currentRang;
        }
        int startI = scores.length - 1;
        int m = in.nextInt();
        for (int alice_i = 0; alice_i < m; alice_i++)
        {
            int alice_score = in.nextInt();
            for (int i = startI; i >= 0; i--)
            {
                int score = scores[i];
                int rang = rangs[i];
                
                if (alice_score < score)
                {
                    System.out.println(rang + 1);
                    startI = i;
                    break;
                }
                else if (alice_score == score)
                {
                    System.out.println(rang);
                    startI = i;
                    break;
                }
                else if (i == 0)
                {
                    System.out.println(1);                    
                    break;
                }
            }
        }

        in.close();
    }
}
