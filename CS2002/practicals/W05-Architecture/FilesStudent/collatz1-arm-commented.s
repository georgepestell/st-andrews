// Student ID: 200007413
// 
// Please comment assembly code 
// For Part 3 you are not required to comment function collatz
//
// Comments start with // 
// You are not required to comment assembler directives 
// For full details of requirements please see practical spec.

	.text
	.file	"collatz.c"
	.globl	collatz
	.p2align	2
	.type	collatz,@function
//
// comments not required for function collatz (below) in Part 1
//

collatz:
	.cfi_startproc
	stp	x29, x30, [sp, #-32]!
	stp	x20, x19, [sp, #16]
	mov	x29, sp
	.cfi_def_cfa w29, 32
	.cfi_offset w19, -8
	.cfi_offset w20, -16
	.cfi_offset w30, -24
	.cfi_offset w29, -32
	adrp	x8, mult
	str	w1, [x8, :lo12:mult]
	adrp	x8, add
	mov	x19, x4
	adrp	x20, array_length
	adrp	x9, div
	str	w2, [x8, :lo12:add]
	adrp	x8, array
	cmp	w0, #1
	str	wzr, [x20, :lo12:array_length]
	str	w3, [x9, :lo12:div]
	str	x5, [x8, :lo12:array]
	b.lt	.LBB0_2
	bl	collatz_recurse
	b	.LBB0_3
.LBB0_2:
	mov	w0, wzr
.LBB0_3:
	ldr	w8, [x20, :lo12:array_length]
	and	w0, w0, #0x1
	str	w8, [x19]
	ldp	x20, x19, [sp, #16]
	ldp	x29, x30, [sp], #32
	ret
.Lfunc_end0:
	.size	collatz, .Lfunc_end0-collatz
	.cfi_endproc
//
// comments not required for function collatz (above) in Part 3
//

//
// comments are required for function collatz_recurse (below)
//
	.p2align	2
	.type	collatz_recurse,@function
collatz_recurse:
	.cfi_startproc
	stp	x29, x30, [sp, #-16]!           // Store frame-pointer & link-register in the stack
	mov	x29, sp                         // Set frame-pointer to stack-pointer (register-register) 
	.cfi_def_cfa w29, 16
	.cfi_offset w30, -8
	.cfi_offset w29, -16
	adrp	x8, array_length              // x8 = address of label array_length
	ldrsw	x9, [x8, :lo12:array_length]  // x9 = array_length 
	cmp	w9, #199                        // Compare array_length to MAXARRAYSIZE (200) (immediate mode)
                                      // Compiler decided to use <= so number is 199
	b.le	.LBB1_2                       // If (array_length < 200), jump to .LBB1_2
.LBB1_1:
	mov	w0, wzr                         // w0 = 0 : set return value to 0
	b	.LBB1_8                           // Jump to .LBB1_8
.LBB1_2:
	adrp	x10, array                    // x10 = address of label array
	ldr	x10, [x10, :lo12:array]         // x10 = array
	add	w11, w9, #1                     // w11 = array_length + 1 (immediate)
	cmp	w0, #1                          // Compare current to 1
	str	w0, [x10, x9, lsl #2]           // Store current in the stack
	str	w11, [x8, :lo12:array_length]   // Store array_length + 1 in the stack
	b.eq	.LBB1_8                       // If (current == 1) jump to .LBB1_8 to return true
                                      // as w0 (return register) is already 1
	adrp	x8, div                       // x8 = address of label div
	ldr	w9, [x8, :lo12:div]             // w9 = div
	sdiv	w8, w0, w9                    // w8 = div / current (ignores remainder)
	msub	w9, w8, w9, w0                // w9 = div - ((div / current) * current) = current%div
	cbz	w9, .LBB1_6                     // If current is a multiple of div, jump to .LBB1_6
                                      // aka. if (current%div == 0)
	adrp	x8, add                       // x8 = address of label add
	ldr	w8, [x8, :lo12:add]             // w8 = add
	adrp	x9, mult                      // x9 = address of label mult
	ldr	w9, [x9, :lo12:mult]            // w9 = mult
	mov	w10, #2147483647                // w10 = INT_MAX (2147483647)
	sub	w10, w10, w8                    // w10 = INT_MAX - add
	sdiv	w10, w10, w9                  // w10 = (INT_MAX - add) / mult
	cmp	w10, w0                         // Compare (INT_MAX - add) / mult to current
	b.lt	.LBB1_1                       // if ((INT_MAX - add) / mult) - current < 0) jump to .LBB1_1
                                      // aka. if (current > (INT_MAX - add) / mult)
	madd	w0, w9, w0, w8                // w0 = current * mult + add
	b	.LBB1_7
.LBB1_6:
	mov	w0, w8                          // w0 = div / current
.LBB1_7:
	bl	collatz_recurse                 // Call collatz recurse
.LBB1_8:
	and	w0, w0, #0x1                    // w0 = w0 (has no effect)
	ldp	x29, x30, [sp], #16             // Restore x29 and x30 to follow convention
	ret                                 // Return
.Lfunc_end1:
	.size	collatz_recurse, .Lfunc_end1-collatz_recurse
	.cfi_endproc

	.type	array_length,@object
	.local	array_length
	.comm	array_length,4,4
	.type	mult,@object
	.local	mult
	.comm	mult,4,4
	.type	add,@object
	.local	add
	.comm	add,4,4
	.type	div,@object
	.local	div
	.comm	div,4,4
	.type	array,@object
	.local	array
	.comm	array,8,8
	.ident	"clang version 12.0.1 (Red Hat 12.0.1-4.module_el8.4.0+2600+cefb5d4c)"
	.section	".note.GNU-stack","",@progbits
	.addrsig
