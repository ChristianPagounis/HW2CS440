package hw2.chess.agents.test.capture;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.State.StateView;
import hw2.chess.agents.ChessAgent;
import hw2.chess.game.Game;
import hw2.chess.game.move.Move;
import hw2.chess.game.move.MoveType;
import hw2.chess.game.piece.Piece;
import hw2.chess.game.piece.PieceType;
import hw2.chess.game.planning.Planner;
import hw2.chess.game.player.Player;
import hw2.chess.game.player.PlayerType;

public class TestQueenCaptureAgent extends ChessAgent {

	private static final long serialVersionUID = -329240707297539858L;
	private static final int timePlayerHasInMS = Integer.MAX_VALUE;

	private final PlayerType playerType;
	private boolean hasPawnCaptured;
	private Player myPlayer;

	public TestQueenCaptureAgent(int playerID, String[] args)
	{
		super(playerID);
		String playerTypeString = null;
		if(args.length == 2)
		{
			playerTypeString = args[1];
		} else
		{
			System.err.println("TestQueenCaptureAgent.TestQueenCaptureAgent [ERROR]: not enough arguments. Must specify player type");
			System.exit(-1);
		}

		this.playerType = PlayerType.valueOf(playerTypeString);
		this.hasPawnCaptured = false;
		this.myPlayer = null;

		System.out.println("Constructed TestQueenCaptureAgent(teamColor=" + this.getPlayerType() + ")");
	}

	private boolean hasPawnCaptured() { return this.hasPawnCaptured; }
	private void recordPawnHasCaptured() { this.hasPawnCaptured = true; }

	@Override
	protected Player getPlayer() { return this.myPlayer; }

	@Override
	protected Move getChessMove(StateView state)
	{
		Game game = Planner.getPlanner().getGame(state, TestQueenCaptureAgent.timePlayerHasInMS);
		List<Move> pawnMoves = game.getAllMovesForPieceType(this.getPlayer(), PieceType.QUEEN);

		List<Move> pawnCaptureMoves = new LinkedList<Move>();
		for(Move move : pawnMoves)
		{
			if(move.getType().equals(MoveType.CAPTUREMOVE))
			{
				pawnCaptureMoves.add(move);
			}
		}

		System.out.println("number of capture moves available for pawns=" + pawnCaptureMoves.size());
		for(Move move: pawnCaptureMoves)
		{
			System.out.println("pawn capture move=" + move);
		}

		// choose randomly amongst the available knight moves
		int index = (int)(Math.random() * pawnCaptureMoves.size());
		return pawnCaptureMoves.get(index);
	}

	@Override
	protected PlayerType getPlayerType()
	{
		return this.playerType;
	}

	@Override
	public Map<Integer, Action> initialStep(StateView state, HistoryView history)
	{
		Game game = Planner.getPlanner().getGame(state, TestQueenCaptureAgent.timePlayerHasInMS);
		game.registerPlayer(this.getPlayerNumber(), this.getPlayerType(), state);
		this.myPlayer = game.getPlayer(this.getPlayerType());
		return null;
	}

	@Override
	public void loadPlayerData(InputStream stream)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Integer, Action> middleStep(StateView state, HistoryView history)
	{
		Map<Integer, Action> actions = new HashMap<Integer, Action>();
		// System.out.println("TestKnightCaptureAgent.middleStep [INFO] init");

		Action action = Planner.getPlanner().getAction(this.getPlayer(), state);
		if(action != null)
		{
			// do something
			System.out.println("TestQueenCaptureAgent.middleStep [INFO] current action=" + action);
			actions.put(action.getUnitId(), action);
		} else if(Planner.getPlanner().canSubmitMove()) // only allow the white player to move for now
		{
			if(!this.hasPawnCaptured() && this.getPlayerType() == PlayerType.WHITE)
			{
				Move move = this.getChessMove(state);
				System.out.println("TestQueenCaptureAgent.middleStep [INFO] selected move=" + move);
	
				System.out.println("TestQueenCaptureAgent.middleStep [INFO] getPlanner().canSubmitMove()=" + Planner.getPlanner().canSubmitMove());
				if(Planner.getPlanner().canSubmitMove())
				{
					Planner.getPlanner().submitMove(move, Planner.getPlanner().getGame());
				}

				this.recordPawnHasCaptured();
			} else if(this.getPlayerType() == PlayerType.WHITE)
			{
				// kill pieces to end the game
				for(Piece piece : Planner.getPlanner().getGame().getBoard().getPieces(this.getPlayer()))
				{
					actions.put(Planner.getPlanner().getGame().getUnitID(piece.getPlayer(), piece.getPieceID()),
							Action.createPrimitiveAttack(Planner.getPlanner().getGame().getUnitID(piece.getPlayer(), piece.getPieceID()),
									Planner.getPlanner().getGame().getUnitID(piece.getPlayer(), piece.getPieceID())));
				}
			}
		}
		return actions;
	}

	@Override
	public void savePlayerData(OutputStream stream)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void terminalStep(StateView state, HistoryView history)
	{
		// TODO Auto-generated method stub

	}

}
