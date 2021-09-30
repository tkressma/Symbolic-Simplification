# Symbolic-Simplification
This program performs the symbolic simplification and evaluation of boolean expressions using <i>and</i>, <i>or</i>, and <i>not</i>.

The following shows the particular forms and their equivalent functions produced by the program.

<ol> 
  <h3>Length 1 Pattern Examples</h3>
  <li>(or true) => true</li>
  <li>(or false) => false</li>
  <li>(or x) => x</li>
  <li>(and true) => true</li>
  <li>(and false) => false</li>
  <li>(and x) => x</li>
  <li>(not false) => true</li>
  <li>(not true) => false</li>
  <li>(not (and x y)) => (or (not x) (not y))</li>
  <li>(not (or x y)) => (and (not x) (not y))</li>
  <li>(not (not x)) => x</li>
  <h3>Length 2 Pattern Examples</h3>
  <li>(or x false) => x</li>
  <li>(or false x) => x</li>
  <li>(or true x) => true</li>
  <li>(or x true) => true</li>
  <li>(and x false) => false</li>
  <li>(and false x) => false</li>
  <li>(and x true) => x</li>
  <li>(and true x) => x</li>
  <h3>Length 3 Pattern Examples</h3>
  <li>(or x y true) => true</li>
  <li>(or x false y) => (or x y)</li>
  <li>(and false x y) => false</li>
  <li>(and x true y) => (and x y)</li>
 </ol>





