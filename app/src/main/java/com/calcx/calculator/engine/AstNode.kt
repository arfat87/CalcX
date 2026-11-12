package com.calcx.calculator.engine

sealed class AstNode {
    data class Number(val value: Double) : AstNode()
    data class Constant(val name: String, val value: Double) : AstNode()
    data class UnaryOp(val op: TokenType, val operand: AstNode) : AstNode()
    data class BinaryOp(val left: AstNode, val op: TokenType, val right: AstNode) : AstNode()
    data class PostfixOp(val operand: AstNode, val op: TokenType) : AstNode()
    data class FunctionCall(val name: String, val args: List<AstNode>) : AstNode()
}
