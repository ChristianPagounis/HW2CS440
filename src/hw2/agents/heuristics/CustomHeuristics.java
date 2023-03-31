package hw2.agents.heuristics;

import java.util.Collections;
import java.util.Comparator;

import hw2.chess.search.DFSTreeNode;

public class CustomHeuristics
{
	public static List<DFSTreeNode> orderChildrenWithHeuristics(List<DFSTreeNode> children)
	Collections.sort(children, new Comparator<DFSTreeNode>()
	{
		public int compare(DFSTreeNode a, DFSTreeNode b)
		{
			//** int utilCompare = Double.compare(a.getGameState().getUtilityValue(), b.getGameState().getUtilityValue()); **//
			//** return a.getGameState().getPlayer() == GameState.FOOTMANPLAYER ? utilCompare : -1 * utilCompare; **//
		}
	});
	return children;
}
	/**
	 * TODO: implement me! The heuristics that I wrote are useful, but not very good for a good chessbot.
	 * Please use this class to add your heuristics here! I recommend taking a look at the ones I provided for you
	 * in DefaultHeuristics.java (which is in the same directory as this file)
	 */
	public static double getHeuristicValue(DFSTreeNode node)
	{
		// please replace this!
		return DefaultHeuristics.getHeuristicValue(node);
	}

}
