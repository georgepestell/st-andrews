# Student ID: 200007413 
# 
# Please comment assembly code 
#
# Comment character is # 
# You are not required to comment assembler directives 
# For full details of requirements please see practical spec.

	.text
	.file	"collatz.c"
	.globl	collatz
	.p2align	4, 0x90
	.type	collatz,@function
collatz:
	.cfi_startproc
	pushq	%rbp                  # Push old base pointer to the stack
	.cfi_def_cfa_offset 16
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp            # Set new base pointer to stack pointer
	.cfi_def_cfa_register %rbp  
	pushq	%rbx                  # Push rbx to the stack
	.cfi_offset %rbx, -24
	testl	%edi, %edi            # Compare start to 0
	jle	.LBB0_8                 # If (start <= 0) jump to .LBB0_8 to skip recursion
	movl	$2147483647, %r11d    # %r11d = INT_MAX (2147483647)
  movl	%edx, %r10d           # %r10d = in_add 
	subl	%edx, %r11d           # %r11d = INT_MAX - in_add
	xorl	%ebx, %ebx            # initialize array_length = 0
	jmp	.LBB0_4                 # Jump to .LBB0_4
	.p2align	4, 0x90
.LBB0_2:
	movl	%eax, %edi            # %edi = current / in_div
.LBB0_3:
	addq	$1, %rbx              # array_length += 1
	cmpq	$200, %rbx            # Compare array_length to the MAXARRAYSIZE (200) 
	je	.LBB0_10                # If array is already full, jump to .LBB0_10 
                              # aka. if (array_length == MAXARRAYSIZE)
.LBB0_4:
	movl	%edi, (%r9,%rbx,4)    # (in_array + array_length * 4) = current 
                              # aka. array[array_length] = current
                              # first time called: current = start
	cmpl	$1, %edi              # Compare current to 1
	je	.LBB0_9                 # If (current == 1) jump to .LBB0_9 to return true
	movl	%edi, %eax            # %eax = current 
	cltd                        # Store the sign of current in edx
	idivl	%ecx                  # %eax = (current / in_div) (truncated remainder)
                              # %edx = (current % in_div) (remainder of division)
	testl	%edx, %edx            # Compare remainder of (current % in_div) to 0
	je	.LBB0_2                 # If current is a multiple of in_div, jump to .LBB0_2
                              # to add current / in_div to array
                              # aka. if (current % div == 0)
	movl	%r11d, %eax           # %eax = INT_MAX
	cltd                        # Store the sign of the value INT_MAX into edx
	idivl	%esi                  # %eax = INT_MAX / in_mult (truncated remainder)
                              # %edx = (INT_MAX % in_mult) (remainder of division)
	cmpl	%edi, %eax            # Compare current to (INT_MAX / in_mult)
	jl	.LBB0_11                # if (current > (INT_MAX / in_mult)) jump to .LBB0_11
	imull	%esi, %edi            # %edi = current * in_mult
	addl	%r10d, %edi           # %edi = current * in_mult + in_add
	jmp	.LBB0_3                 # Jump to .LBB0_3 to add (current * in_mult + in_add) to array
.LBB0_8:
	xorl	%ebx, %ebx            # array_length = 0
	jmp	.LBB0_12                # Jump to .LBB0_12 to return false
.LBB0_9:
	addl	$1, %ebx              # array_length += 1
	movb	$1, %al               # set first bit of %eax to 1: set return value to true
	jmp	.LBB0_13
.LBB0_10:
	movl	$200, %ebx            # array_length = MAXARRAYSIZE (200)
	jmp	.LBB0_12                # Jump to .LBB0_12
.LBB0_11:
	addl	$1, %ebx              # array_length += 1
.LBB0_12:
	xorl	%eax, %eax            # %eax = 0: set return value to false
.LBB0_13:
	movl	%ebx, (%r8)           # *in_length = array_length
	popq	%rbx                  # Pop previous %rbx value from stack
	popq	%rbp                  # Pop previous %rbp value from stack
	.cfi_def_cfa %rsp, 8
	retq                        # Return from function
.Lfunc_end0:
	.size	collatz, .Lfunc_end0-collatz
	.cfi_endproc

	.ident	"clang version 12.0.1 (Red Hat 12.0.1-4.module_el8.4.0+2600+cefb5d4c)"
	.section	".note.GNU-stack","",@progbits
	.addrsig
