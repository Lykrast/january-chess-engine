package com.nullprogram.chess;

import com.google.gson.JsonDeserializer;
import com.nullprogram.chess.pieces.MoveTypeBishop;
import com.nullprogram.chess.pieces.MoveTypeKing;
import com.nullprogram.chess.pieces.MoveTypeKnight;
import com.nullprogram.chess.pieces.MoveTypePawn;
import com.nullprogram.chess.pieces.MoveTypeRook;
import com.nullprogram.chess.resources.MoveTypeDeserializer;

public abstract class MoveType implements JsonDeserializer<MoveType> {

    /**
     * Determine moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public abstract MoveList getMoves(final Piece p, final MoveList list);
    
    /**
     * Registers all deserializers for Json loading.
     */
    public static void registerDeserializers()
    {
    	MoveTypeDeserializer.registerDeserializer("Pawn", new MoveTypePawn());
    	MoveTypeDeserializer.registerDeserializer("Rook", new MoveTypeRook());
    	MoveTypeDeserializer.registerDeserializer("Knight", new MoveTypeKnight());
    	MoveTypeDeserializer.registerDeserializer("Bishop", new MoveTypeBishop());
    	MoveTypeDeserializer.registerDeserializer("King", new MoveTypeKing());
    }

}
