# Student ID: 200007413
#
# Please comment assembly code
# For Part 1 you are not required to comment function "collatz"
#
# Comment character is #
# You are not required to comment assembler directives
# For full details of requirements please see practical spec.

.text
.file "collatz.c"
.globl  collatz
.p2align  4, 0x90
.type collatz,@function

#
# comments not required for function collatz (below) in Part 1
#

collatz:
  .cfi_startproc
  pushq %rbp
  .cfi_def_cfa_offset 16
  .cfi_offset %rbp, -16
  movq  %rsp, %rbp
  .cfi_def_cfa_register %rbp
  subq  $48, %rsp
  movl  %edi, -4(%rbp)
  movl  %esi, -8(%rbp)
  movl  %edx, -12(%rbp)
  movl  %ecx, -16(%rbp)
  movq  %r8, -24(%rbp)
  movq  %r9, -32(%rbp)
  movl  $0, array_length
  movl  -8(%rbp), %eax
  movl  %eax, mult
  movl  -12(%rbp), %eax
  movl  %eax, add
  movl  -16(%rbp), %eax
  movl  %eax, div
  movq  -32(%rbp), %rax
  movq  %rax, array
  movb  $0, -33(%rbp)
  cmpl  $1, -4(%rbp)
  jl  .LBB0_2
  movl  -4(%rbp), %edi
  callq collatz_recurse
  andb  $1, %al
  movb  %al, -33(%rbp)
.LBB0_2:
  movl  array_length, %ecx
  movq  -24(%rbp), %rax
  movl  %ecx, (%rax)
  movb  -33(%rbp), %al
  andb  $1, %al
  movzbl  %al, %eax
  addq  $48, %rsp
  popq  %rbp
  .cfi_def_cfa %rsp, 8
  retq
.Lfunc_end0:
  .size collatz, .Lfunc_end0-collatz
  .cfi_endproc
#
# comments not required for function collatz (above) in Part 1
#

#
# comments are required for function collatz_recurse (below)
#

  .p2align  4, 0x90
  .type collatz_recurse,@function
collatz_recurse:
  .cfi_startproc
  pushq %rbp                  # Push old base pointer to the stack
  .cfi_def_cfa_offset 16
  .cfi_offset %rbp, -16
  movq  %rsp, %rbp            # Set new base pointer as the current stack pointer
  .cfi_def_cfa_register %rbp
  subq  $16, %rsp             # Allocate space in red zone for arguments
                              # (aligned to 16-bits)
  movl  %edi, -8(%rbp)        # Copy current to stack
  cmpl  $200, array_length    # Compare array_length to MAXARRAYSIZE constant (200)
  jl  .LBB1_2                 # if (array_length < MAXARRAYSIZE) jump to .LBB1_2
  movb  $0, -1(%rbp)          # -1(%rbp) = 0
  jmp .LBB1_9                 # Jump to .LBB1_9
.LBB1_2:
  movl  -8(%rbp), %edx        # %edx = current
  movq  array, %rax           # %rax = array
  movslq  array_length, %rcx  # %rcx = array_length
  movl  %edx, (%rax,%rcx,4)   # (array + 4 * array_length) = array[array_length] = current
  movl  array_length, %eax    # %eax = array_length
  addl  $1, %eax              # %eax = array_length + 1
  movl  %eax, array_length    # array_length = array_length + 1
  cmpl  $1, -8(%rbp)          # Compare current to the number 1
  jne .LBB1_4                 # if (current != 1) jump to .LBB1_4
  movb  $1, -1(%rbp)          # -1(%rbp) = 1 : set return value to true
  jmp .LBB1_9                 # Jump to .LBB1_9
.LBB1_4:
  movl  -8(%rbp), %eax        # %eax = current
  cltd                        # Store the sign of current in eax to edx
  idivl div                   # %eax = current / div;
                              # %edx = current%div (remainder of division)
  cmpl  $0, %edx              # Compare current%div with 0
  jne .LBB1_6                 # if current is a multiple of div, jump to .LBB1_6
                              # aka. if (current%div != 0)
  movl  -8(%rbp), %eax        # %eax = current
  cltd                        # Store the sign of current in eax to edx
  idivl div                   # %eax = current / div (truncated remainder)
                              # %edx = current%div (remainder of division)
  movl  %eax, %edi            # %edi = current / div (truncated remainder)
  callq collatz_recurse       # Call collatz_recurse
  andb  $1, %al               # After call, %al = 1
  movb  %al, -1(%rbp)         # -1(%rbp) = 1: set return value to true
  jmp .LBB1_9                 # Jump to .LBB1_9 (return)
.LBB1_6:
  movl  -8(%rbp), %eax        # %eax = current
  movl  %eax, -12(%rbp)       # -12(%rbp) = current
  movl  $2147483647, %eax     # %eax = INT_MAX (2147483647)
  subl  add, %eax             # $eax = INT_MAX - add
  cltd                        # Store the sign of the value eax into edx
  idivl mult                  # %eax = (INT_MAX - add) / mult (truncated remainder)
  movl  %eax, %ecx            # %ecx = (INT_MAX - add) / mult (truncated remainder)
  movl  -12(%rbp), %eax       # %eax = current
  cmpl  %ecx, %eax            # Compare (INT_MAX - add) / mult to current
  jle .LBB1_8                 # if (current <= (INT_MAX - add) / mult) jump to .LBB1_8
                              # otherwise (current > (INT_MAX - add) / mult) so continue
  movb  $0, -1(%rbp)          # -1(%rbp) = 0: set return value to false
  jmp .LBB1_9                 # Jump to .LBB1_9 (return)
.LBB1_8:
  movl  -8(%rbp), %edi        # %edi = current
  imull mult, %edi            # %edi = current * mult
  addl  add, %edi             # %edi = current * mult + add
  callq collatz_recurse       # Call collatz_recurse
  andb  $1, %al               # %al = %al
  movb  %al, -1(%rbp)         # -1(%rbp) = %al
.LBB1_9:
  movb  -1(%rbp), %al         # %al = -1(%rbp): set return value
  andb  $1, %al               # %al = %al
  movzbl  %al, %eax           # %eax = %al = -1(%rbp): set return value
  addq  $16, %rsp             # Unallocate the stack frame red-zone space
  popq  %rbp                  # Restore the base-pointer value to caller value
  .cfi_def_cfa %rsp, 8
  retq                        # Return from this collatz_recurse function call
.Lfunc_end1:
  .size collatz_recurse, .Lfunc_end1-collatz_recurse
  .cfi_endproc

  .type array_length,@object
  .local  array_length
  .comm array_length,4,4
  .type mult,@object
  .local  mult
  .comm mult,4,4
  .type add,@object
  .local  add
  .comm add,4,4
  .type div,@object
  .local  div
  .comm div,4,4
  .type array,@object
  .local  array
  .comm array,8,8
  .ident  "clang version 12.0.1 (Red Hat 12.0.1-4.module_el8.4.0+2600+cefb5d4c)"
  .section  ".note.GNU-stack","",@progbits
  .addrsig
  .addrsig_sym collatz_recurse
  .addrsig_sym array_length
  .addrsig_sym mult
  .addrsig_sym add
  .addrsig_sym div
  .addrsig_sym array
