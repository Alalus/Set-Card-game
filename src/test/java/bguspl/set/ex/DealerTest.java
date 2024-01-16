package bguspl.set.ex;
import java.util.*;
import bguspl.set.ex.Table;
import bguspl.set.ex.Player;
import bguspl.set.Config;
import bguspl.set.Env;
import bguspl.set.UserInterface;
import bguspl.set.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DealerTest {

    private Dealer dealer;

    private Table table;

    private Player[] players;


    @BeforeEach
    void setUp() {
        Properties properties = new Properties();
        properties.put("Rows", "3");
        properties.put("Columns", "4");
        properties.put("FeatureSize", "3");
        properties.put("FeatureCount", "4");
        properties.put("TableDelaySeconds", "0");
        properties.put("PlayerKeys1", "81,87,69,82");
        properties.put("PlayerKeys2", "85,73,79,80");
        TableTest.MockLogger logger = new TableTest.MockLogger();
        Config config = new Config(logger, properties);
        Integer[] slotToCard = new Integer[config.tableSize];
        Integer[] cardToSlot = new Integer[config.deckSize];
        Env env = new Env(logger, config, new TableTest.MockUserInterface(), new TableTest.MockUtil());
        table = new Table(env, slotToCard, cardToSlot);


        players = new Player[2];
        players[0] = new Player(env, dealer, table, 0, true);
        players[1] = new Player(env, dealer, table, 1, true);

        dealer = new Dealer(env, table, players);

    }

    @Test
    void announceWinners() {

        //draw
        players[0].point();
        players[0].point();
        players[1].point();
        players[1].point();

        //player0 = player1 = 2
        int[] expectedWinners1 = new int[2];
        expectedWinners1[0] = 0;
        expectedWinners1[1] = 1;

        // call the method we are testing
        int[] winners1 =  dealer.announceWinners();

        boolean same1 = ((winners1[0] == expectedWinners1[0]) && (winners1[1] == expectedWinners1[1]));

        assertEquals(true, same1);

        // ------------

        //player1 wins
        players[1].point();

        int[] expectedWinners2 = new int[1];
        expectedWinners2[0] = 1;

        // call the method we are testing
        int[] winners2 =  dealer.announceWinners();

        boolean same2 = (winners2[0] == expectedWinners2[0]);

        assertEquals(true, same2);

    }

    @Test
    void addPlayetToQ() {

        // call the method we are testing
        dealer.addPlayerToQ(players[0]);
        dealer.addPlayerToQ(players[1]);

        Queue<Player> expectedQ = new LinkedList<Player>();
        expectedQ.add(players[0]);
        expectedQ.add(players[1]);

        // call the method we are testing
        Queue<Player> qPlayersToCheck = dealer.getPlayersQ();
        
        assertEquals(expectedQ, qPlayersToCheck);
    }
    
}

 