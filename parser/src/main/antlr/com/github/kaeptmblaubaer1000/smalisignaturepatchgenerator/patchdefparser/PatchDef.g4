/*
 * Parser Rules
 */

grammar PatchDef;

rootParser : (patch* | WHITESPACE*) EOF ;

patch : ' ' ;


/*
 * Lexer Rules
 */

WHITESPACE : [\p{White_Space}] ;
