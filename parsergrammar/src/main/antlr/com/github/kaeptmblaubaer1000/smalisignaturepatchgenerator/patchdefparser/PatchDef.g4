/*
 * Parser Rules
 */

grammar PatchDef;

@header {
    package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser;
}

rootParser : (patch | whitespace)* EOF ;

patch : 'patch' WHITESPACE* '(' WHITESPACE* StringLiteral WHITESPACE* ')' WHITESPACE* '{' WHITESPACE* patchBody whitespace* '}' NL ;

patchBody : (assignment | whitespace)*;

assignment : (humanNameAssignment) NL ;

humanNameAssignment : 'humanName' WHITESPACE* '=' WHITESPACE* StringLiteral ;

whitespace : WHITESPACE | NL ;

/*
 * Lexer Rules
 */

WHITESPACE : [ \t] ;
NL : '\r' | '\n' | '\r\n' | WHITESPACE ';' WHITESPACE ;

// The following part is derived from
// https://github.com/antlr/grammars-v4/blob/8d2396883090584f83ca4575edf74baa7ccc14f2/java8/Java8.g4
// but I've removed \f from the escapes since this DSL should be compatible with Kotlin and Groovy, because it's missing
// in Kotlin and you don't need it in this DSL.

/*
 * [The "BSD license"]
 *  Copyright (c) 2014 Terence Parr
 *  Copyright (c) 2014 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

StringLiteral : '"' StringCharacters? '"' ;
fragment StringCharacters : StringCharacter+ ;
fragment StringCharacter : ~["\\\r\n] | EscapeSequence ;
fragment EscapeSequence : '\\' [btnr"'\\] | OctalEscape | UnicodeEscape ;
fragment OctalEscape : '\\' OctalDigit | '\\' OctalDigit OctalDigit | '\\' ZeroToThree OctalDigit OctalDigit ;
fragment ZeroToThree : [0-3] ;
fragment UnicodeEscape : '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit ;
fragment OctalDigit : [0-7] ;
fragment HexDigit : [0-9a-fA-F] ;