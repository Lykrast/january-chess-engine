package com.nullprogram.chess.pieces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece.Side;

public class AIHints {
	//Inspired by https://www.chessprogramming.org/Simplified_Evaluation_Function
	//pawn = pushPromotion + pushCenter
	//knight = occupyCenter
	//bishop = occupyCenterSoft + avoidBorders
	//queen = avoidBorders
	private boolean occupyCenter, occupyCenterSoft, avoidBorders, occupyCenterRanks, occupyCenterRanksSoft, avoidBordersRanks, pushPromotion, pushCenter;
	
	public AIHints() {}
	
	public double evaluate(Board b, Position pos, Side s) {
		double tot = 0;
		if (occupyCenter) tot += occupyCenter(b, pos);
		if (occupyCenterSoft) tot += occupyCenterSoft(b, pos);
		if (avoidBorders) tot += avoidBorders(b, pos);
		if (occupyCenterRanks) tot += occupyCenterRanks(b, pos);
		if (occupyCenterRanksSoft) tot += occupyCenterRanksSoft(b, pos);
		if (avoidBordersRanks) tot += avoidBordersRanks(b, pos);
		if (pushPromotion) tot += pushPromotion(b, pos, s);
		if (pushCenter) tot += pushCenter(b, pos, s);
		return tot;
	}
	
	private double pushPromotion(Board b, Position pos, Side s) {
		int prom = distToPromotion(b, pos, s);
		if (prom <= 1) return 0.5;
		int midY = b.getHeight() / 2;
		if (prom < midY) return 0.2*(1-(prom/(double)midY));
		return 0;
	}
	
	private double pushCenter(Board b, Position pos, Side s) {
		int prom = distToPromotion(b, pos, s);
		int midY = b.getHeight() / 2;
		//Push center pawns
		if (distXToCenter(b, pos) < Math.max(b.getWidth() / 4 - 1, 1)) {
			if (prom >= midY) return -0.8*((prom/(double)midY)-1);
			else return 0.2 + 0.1*(1-(prom/(double)midY));
		}
		return 0;
	}
	
	private double occupyCenter(Board b, Position pos) {
		return 0.2 - 0.4*(distXToCenter(b, pos)/(double)b.getWidth() + distYToCenter(b, pos)/(double)b.getHeight());
	}
	
	private double occupyCenterSoft(Board b, Position pos) {
		int distX = distXToCenter(b, pos);
		int distY = distYToCenter(b, pos);
		if (distX <= Math.max(b.getWidth() / 4 - 1, 1) && distY <= Math.max(b.getHeight() / 4 - 1, 1)) return 0.1 - 0.4*(distX/(double)b.getWidth() + distY/(double)b.getHeight());
		else return 0;
	}
	
	private double avoidBorders(Board b, Position pos) {
		return -0.1*borderStatus(b, pos);
	}
	
	private double occupyCenterRanks(Board b, Position pos) {
		return 0.2 - 0.2*(distYToCenter(b, pos)/(double)b.getHeight());
	}
	
	private double occupyCenterRanksSoft(Board b, Position pos) {
		int distY = distYToCenter(b, pos);
		if (distY <= Math.max(b.getHeight() / 4 - 1, 1)) return 0.1 - 0.2*(distY/(double)b.getHeight());
		else return 0;
	}
	
	private double avoidBordersRanks(Board b, Position pos) {
		return -0.1*borderYStatus(b, pos);
	}
	
//	private int distToCenter(Board b, Position pos) {
//		int midX = b.getWidth() / 2;
//		int midY = b.getHeight() / 2;
//		//Even board: middle is multiple squares so might need to -1 to get our corresponding one
//		//Odd board: we should get the correct mid square
//		if (b.getWidth() % 2 == 0 && pos.getX() < midX) midX--;
//		if (b.getHeight() % 2 == 0 && pos.getY() < midY) midY--;
//		return Math.abs(pos.getX() - midX) + Math.abs(pos.getY() - midY);
//	}
	
	private int distXToCenter(Board b, Position pos) {
		int midX = b.getWidth() / 2;
		//Even board: middle is multiple squares so might need to -1 to get our corresponding one
		//Odd board: we should get the correct mid square
		if (b.getWidth() % 2 == 0 && pos.getX() < midX) midX--;
		return Math.abs(pos.getX() - midX);
	}
	
	private int distYToCenter(Board b, Position pos) {
		int midY = b.getHeight() / 2;
		//Even board: middle is multiple squares so might need to -1 to get our corresponding one
		//Odd board: we should get the correct mid square
		if (b.getHeight() % 2 == 0 && pos.getY() < midY) midY--;
		return Math.abs(pos.getY() - midY);
	}
	
	private int distToPromotion(Board b, Position pos, Side s) {
		int ty = s == Side.WHITE ? b.getHeight()-1 : 0;
		return Math.abs(pos.getY() - ty);
	}
	
	private int borderStatus(Board b, Position pos) {
		int x = pos.getX();
		int y = pos.getY();
		return (x == 0 || x == b.getWidth()-1 ? 1 : 0) + (y == 0 || y == b.getHeight()-1 ? 1 : 0);
	}
	
	private int borderYStatus(Board b, Position pos) {
		int y = pos.getY();
		return y == 0 || y == b.getHeight()-1 ? 1 : 0;
	}
	
	private void activate(String target) throws JsonParseException {
		if (target.equals("OCCUPY_CENTER")) occupyCenter = true;
		else if (target.equals("OCCUPY_CENTER_SOFT")) occupyCenterSoft = true;
		else if (target.equals("AVOID_BORDERS")) avoidBorders = true;
		else if (target.equals("OCCUPY_CENTER_RANKS")) occupyCenter = true;
		else if (target.equals("OCCUPY_CENTER_RANKS_SOFT")) occupyCenterSoft = true;
		else if (target.equals("AVOID_BORDERS_RANKS")) avoidBorders = true;
		else if (target.equals("PUSH_PROMOTION")) pushPromotion = true;
		else if (target.equals("PUSH_CENTER")) pushCenter = true;
		else throw new JsonParseException("Invalid ai hint : " + target
				+ " - must be OCCUPY_CENTER, OCCUPY_CENTER_SOFT, AVOID_BORDERS, OCCUPY_CENTER_RANKS, OCCUPY_CENTER_RANKS_SOFT, AVOID_BORDERS_RANKS, PUSH_PROMOTION, or PUSH_CENTER"); 
	}
	
	public static AIHints fromJson(JsonObject json) throws JsonParseException {
		JsonElement tmp = json.get("aihints");
		AIHints hints = new AIHints();
		if (tmp != null) {
			if (tmp.isJsonArray()) {
				for (JsonElement elem : tmp.getAsJsonArray()) {
					hints.activate(elem.getAsString());
				}
			}
			else hints.activate(tmp.getAsString());
		}
		return hints;
	}

}
