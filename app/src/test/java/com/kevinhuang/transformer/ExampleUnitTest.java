package com.kevinhuang.transformer;

import com.kevinhuang.ServerUtil.TransformerManager;
import com.kevinhuang.Utility.CommonUtil;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    // Test no Transformer added
    @Test
    public void battle_test_empty() {
        HashMap<String, Transformer> testList = new HashMap<>();
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v0_autobot() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 1, 1, 1, 1, 1, 1, 1, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v0_decepticon() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 1, 1, 1, 1, 1, 1, 1, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_strength_courage() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 10, 10, 10, 10, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_autobot_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_strength_only() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 10, 10, 10, 10, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 4, 1, 1, 1, 1, 3, 1, 1, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_decepticon_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_skill() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 10, 10, 10, 10, 1, 10, 4, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 1, 1, 1, 1, 1, 5, 1, 7, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_autobot_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_overall_rating() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 1, 10, 10, 10, 10, 7, 10, 5, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 2, 1, 1, 1, 1, 5, 1, 5, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_decepticon_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_optimus_prime() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_OPTIMUS_PRIME, 1, 10, 10, 10, 10, 1, 1, 10, 'A', ""));
        testList.put("b", new Transformer("b", "test", 4, 1, 1, 1, 1, 5, 1, 10, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_autobot_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_predaking() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 10, 1, 10, 1, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 10, 2, 1, 1, 1, 2, 1, 10, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_decepticon_won == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_name_all_destroy_tie() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 10, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", CommonUtil.NAME_OPTIMUS_PRIME, 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_all_destroyed_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_1v1_rating_tie_both_destroy_tie() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", "test", 4, 10, 10, 1, 4, 1, 10, 8, 'D', ""));
        testList.put("b", new Transformer("b", "test2", 5, 9, 10, 10, 7, 5, 1, 9, 'A', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_2v3_all_destroy_autobot() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 10, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", CommonUtil.NAME_OPTIMUS_PRIME, 4, 1, 1, 1, 7, 5, 1, 1, 'A', ""));
        testList.put("c", new Transformer("c", "test", 1, 10, 10, 10, 7, 1, 10, 10, 'D', ""));
        testList.put("d", new Transformer("d", "test1", 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        testList.put("e", new Transformer("e", "test2", 1, 10, 10, 10, 6, 1, 10, 10, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_all_destroyed_autobot == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_2v3_tie() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 1, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", CommonUtil.NAME_OPTIMUS_PRIME, 4, 1, 1, 1, 7, 5, 1, 1, 'A', ""));
        testList.put("c", new Transformer("c", "test", 4, 10, 10, 10, 7, 1, 10, 10, 'D', ""));
        testList.put("d", new Transformer("d", "test1", 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        testList.put("e", new Transformer("e", "test2", 4, 10, 10, 10, 6, 1, 10, 1, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_3v4_decepticon() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 5, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", CommonUtil.NAME_OPTIMUS_PRIME, 4, 1, 1, 1, 7, 5, 1, 1, 'A', ""));
        testList.put("c", new Transformer("c", "test", 4, 10, 10, 10, 7, 1, 10, 10, 'D', ""));
        testList.put("d", new Transformer("d", "test1", 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        testList.put("e", new Transformer("e", "test2", 4, 10, 10, 10, 6, 1, 10, 1, 'D', ""));
        testList.put("f", new Transformer("f", "test3", 4, 10, 10, 10, 6, 1, 10, 1, 'A', ""));
        testList.put("g", new Transformer("g", "test4", 4, 10, 10, 10, 2, 1, 10, 1, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_tie == TransformerManager.getInstance().startBattle());
    }

    @Test
    public void battle_test_3v4_duplicate_all_destroy_autobot() {
        HashMap<String, Transformer> testList = new HashMap<>();
        testList.put("a", new Transformer("a", CommonUtil.NAME_PREDAKING, 1, 10, 10, 10, 10, 1, 10, 10, 'D', ""));
        testList.put("b", new Transformer("b", CommonUtil.NAME_PREDAKING, 4, 1, 1, 1, 10, 5, 1, 1, 'A', ""));
        testList.put("c", new Transformer("c", "test", 4, 10, 10, 10, 7, 1, 10, 10, 'D', ""));
        testList.put("d", new Transformer("d", "test1", 4, 1, 1, 1, 1, 5, 1, 1, 'A', ""));
        testList.put("e", new Transformer("e", "test2", 4, 10, 10, 10, 6, 1, 10, 1, 'D', ""));
        testList.put("f", new Transformer("f", "test3", 4, 10, 10, 10, 6, 1, 10, 1, 'A', ""));
        testList.put("g", new Transformer("g", "test4", 4, 10, 10, 10, 2, 1, 10, 1, 'D', ""));
        TransformerManager.getInstance().SetTransformerList(testList);
        assertTrue(R.string.msg_all_destroyed_autobot == TransformerManager.getInstance().startBattle());
    }
}