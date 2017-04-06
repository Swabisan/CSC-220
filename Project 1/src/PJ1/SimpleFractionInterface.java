/* This file specifies methods for SimpleFractionInterface	*/
/* Do not modify this file!!                  			*/

package PJ1;

public interface SimpleFractionInterface 
{
        /** Task: Sets "this" fraction to a given value.
         *  @param num is the integer numerator
         *  @param den is the integer denominator
         *  @throw SimpleFractionException if denominator is 0 */
        public void setSimpleFraction(int num, int den);

        /** Task: Reduce "this" fraction to lowest term, i.e divide the numerator 
                  and denominator by their Greatest Common Divisor */
        public void simplifySimpleFraction();

	/** Task: compute floating value of "this" fraction 
	 *  @return the double floating point value of a fraction */
	public double toDouble();

	/** Task: Adds two fractions.
	 *  @param secondFraction is a fraction that is the second operand of the addition
	 *  @return a new reduced fraction which is the sum of "this" fraction and the secondFraction */
	public SimpleFractionInterface add(SimpleFractionInterface secondFraction);

	/** Task: Subtracts two fractions.
	 *  @param secondFraction a fraction that is the second operand of the subtraction
	 *  @return a new reduced fraction which is the difference of "this" fraction and the second operand */
	public SimpleFractionInterface subtract(SimpleFractionInterface secondFraction);

	/** Task: Multiplies two fractions.
	 *  @param secondFraction a fraction that is the second operand of the multiplication
	 *  @return a new reduced fraction which is the product of "this" fraction and the secondFraction*/
	public SimpleFractionInterface multiply(SimpleFractionInterface secondFraction);

	/** Task: Divides two fractions.
	 *  @param secondFraction a fraction that is the second operand of the division
	 *  @return a new reduced fraction which the quotient of "this" fraction and the secondFraction
         *  @throw SimpleFractionException if secondFraction is 0 */
	public SimpleFractionInterface divide(SimpleFractionInterface secondFraction);


}
