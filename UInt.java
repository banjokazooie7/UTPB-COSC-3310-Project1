/**
 * @auth
 * Oscar Navarro
 * COSC 3310 - Project 1
 */

import java.util.Arrays;

/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * 
 */
public class UInt {

    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;

    // The number of bits used to represent the unsigned integer.
    protected int length;

    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }

    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
        bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
            // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;

            // Deprecated analog method
            /*int p = 0;
            while (Math.pow(2, p) < i) {
                p++;
            }
            p--;
            bits[p] = true;
            i -= Math.pow(2, p);*/
        }
    }

    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }

    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }

    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }

    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }

    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }

    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }
        // In the specific case that this.length is greater, there are additional elements of
        //   this.bits that are not getting ANDed against anything.
        // Depending on the implementation, we may want to treat the operation as implicitly padding
        //   the u.bits array to match the length of this.bits, in which case what we actually
        //   perform is simply setting the remaining indices of this.bits to false.
        // Note that while this logic is helpful for the AND operation if we want to use this
        //   implementation (implicit padding), it is never necessary for the OR and XOR operations.
        if (this.length > u.length) {
            for (int i = u.length; i < this.length; i++) {
                this.bits[this.length - i - 1] = false;
            }
        }
    }

    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }

    public void or(UInt u) {
        //sets each bit in the result to 1 if either of the bits in the operands is 1
        for (int i=0; i < Math.min(this.length, u.length); i++){
            this.bits[this.length - i -1] = this.bits[this.length - i - 1] | u.bits[u.length - i - 1];
        }
    }

    public static UInt or(UInt a, UInt b) {
        int maxLen = Math.max(a.length, b.length);

        //create new boolean arrays of length maxLen to hold the bits
        boolean[] bitsA = new boolean[maxLen];
        boolean[] bitsB = new boolean[maxLen];
        //copy aligns least significant bits
        System.arraycopy(a.bits, 0, bitsA, maxLen - a.length, a.length);
        System.arraycopy(b.bits, 0, bitsB, maxLen - b.length, b.length);

        //holds bits in boolean array
        UInt result = new UInt(0);
        result.length = maxLen;
        result.bits = new boolean[maxLen];
        //checks each bit position and performs OR operation
        for (int i = 0; i < maxLen; i++){
            result.bits[i] = bitsA[i] | bitsB[i];
        }
        return result; 
    }

    public void xor(UInt u) {
        //check from 0 to min length then performs xor for the bits in the objects
        for (int i =0; i < Math.min(this.length, u.length); i++){
            this.bits[this.length - i - 1] = this.bits[this.length - i - 1] ^ u.bits[u.length - i -1];
        }
    }

    public static UInt xor(UInt a, UInt b) {
        int maxLen = Math.max(a.length, b.length);
        //create two new boolean arrays of the maximum length
        boolean[] bitsA = new boolean[maxLen];
        boolean[] bitsB = new boolean[maxLen];
        //both arrays have the same length and are properly aligned
        System.arraycopy(a.bits, 0, bitsA, maxLen - a.length, a.length);
        System.arraycopy(b.bits, 0, bitsB, maxLen - b.length, b.length);

        //creates a object to store the result of the XOR operation
        UInt result = new UInt(0);
        result.length = maxLen;
        result.bits = new boolean[maxLen];
        //checks each bit position and applies the xor
        for (int i = 0; i < maxLen; i++){
            result.bits[i] = bitsA[i] ^ bitsB[i];
        }
        return result;
    }

    public void add(UInt u) {
        int maxLen = Math.max(this.length, u.length);
        //stores result of addition
        boolean[] result = new boolean[maxLen +1];
        //creates new arrays of maxLen size to align the bits
        boolean[] bitsA = new boolean[maxLen];
        boolean[] bitsB = new boolean[maxLen];
        System.arraycopy(this.bits, 0, bitsA, maxLen - this.length, this.length);
        System.arraycopy(u.bits, 0, bitsB, maxLen - u.length, u.length);
        //carry is needed when a 1 needs to be passed over
        boolean carry = false;
        //bitwise addition then stores it in the array
        for(int i = maxLen - 1; i >= 0; i--){
            boolean sum = bitsA[i] ^ bitsB[i] ^ carry;
            carry = (bitsA[i] && bitsB[i]) || (bitsA[i] && carry) || (bitsB[i] && carry);
            result[i + 1] = sum;
        }

        result[0] = carry;
        //replaces current bit with result array then updates length
        if (result[0]){
            this.bits = result;
            this.length = result.length - 1;
        }
    }

    public static UInt add(UInt a, UInt b) {
        //max length of the two objects
        int maxLen = Math.max(a.length, b.length);
        //creates new arrays to store the aligned bit positions of a and b
        boolean[] bitsA = new boolean[maxLen];
        boolean[] bitsB = new boolean[maxLen];
        System.arraycopy(a.bits, 0, bitsA, maxLen - a.length, a.length);
        System.arraycopy(b.bits, 0, bitsB, maxLen - b.length, b.length);
        //new array stores the sum along with carry overflow
        boolean[] result = new boolean[maxLen +1];
        boolean carry = false;
        //loop all the bits of the aligned array starts with LSB of the array
        for(int i = maxLen - 1; i >= 0; i--){
            boolean sum = bitsA[i] ^ bitsB[i] ^ carry;
            //update the carry value
            carry = (bitsA[i] && bitsB[i]) || (bitsA[i] && carry) || (bitsB[i] && carry);
            result[i + 1] = sum;
        }
        result[0] = carry;
        //this will hold the result in a object
        UInt temp = new UInt(0);
        //assigns the result to the array
        if(carry){
            temp.length = result.length;
            temp.bits = result;
        }else{
            //else copy the significant bits of the result 
            temp.length = result.length - 1;
            temp.bits = Arrays.copyOfRange(result, 1, result.length);
        }
        return temp;
    }

    public void negate() {
        //this loop will flip each bit for 2s complement
        for(int i = 0; i < this.length; i++){
            this.bits[i] = !this.bits[i];
        }
        //new object with value of 1 then adds 1 for the negathin required
        UInt newOne = new UInt(1);
        this.add(newOne);
    }

    public void sub(UInt u) {
        //clones the input object 
        UInt negU = UInt.clone(u);
        negU.negate();
        //add negated value
        this.add(negU);
        //checks is the value is negative then resets to zero
        if(this.toInt() < 0){
            this.bits = new boolean[1];
            this.length =1;
        }
    }

    public static UInt sub(UInt a, UInt b) {
        //create clones of the original operands and negate the second one
        UInt result = UInt.clone(a);
        UInt negB = UInt.clone(b);
        negB.negate();
        result.add(negB);
        //convert the result to an Int
        if(result.toInt() < 0){
            return new UInt(0);
        }
        return result;
    }

    public void mul(UInt u) {
        //calculates the max possible length of the product array
        int maxLen = this.length + u.length;
        //aligns the bits to the rightmost positions in the array
        boolean[] multiplies = new boolean[maxLen];
        System.arraycopy(this.bits, 0, multiplies, maxLen - this.length, this.length);
        
        boolean[] multiPlier = new boolean[maxLen];
        System.arraycopy(u.bits, 0, multiPlier, maxLen - u.length, u.length);
        //array stores the final results
        boolean[] product = new boolean[maxLen];
        //
        for(int i = 0; i < maxLen; i++){
            //if the bit is 1 the multiplicand is added to the product
            if(multiPlier[maxLen - i - 1]){
                product = addBinary(product, shiftLeft(multiplies, i));
            }
        }
        //removes leading zeros from the array
        this.bits = deleteZeros(product);
        this.length = this.bits.length;
    }

    public static UInt mul(UInt a, UInt b) {
        //max possible length of the array
        int maxLen = a.length + b.length;
        //stores the bits of a and aligns the rightmost positions of a new array
        boolean[] multiplies = new boolean[maxLen];
        System.arraycopy(a.bits, 0, multiplies, maxLen - a.length, a.length);

        boolean[] multiPlier = new boolean[maxLen];
        System.arraycopy(b.bits, 0, multiPlier, maxLen - b.length, b.length);
        //holds partial products during the loop
        boolean[] product = new boolean[maxLen];
        //loops through each bit of the multiPlier
        for(int i=0; i < maxLen; i++){
            //if the bit is 1 multiPlier is added to the product after being shifted
            if(multiPlier[maxLen - i - 1]){
                product = addBinary(product, shiftLeft(multiplies, i));
            }
        }
        //deletes unnecessary zeros
        product = deleteZeros(product);
        //assign the final result array to result.bits
        UInt result = new UInt(0);
        result.bits = product;
        //change result.length to show the length of the product array
        result.length = product.length;
        return result;
    }
    ////////////////////*********************Helper Methods************************************//////////////////////////////////
    private static boolean[] addBinary(boolean[] a, boolean[] b){
        //makes sure the length of the array will have enough space to hold the addition results
        int maxLen = Math.max(a.length, b.length);
        boolean[] result = new boolean[maxLen + 1];

        boolean carry = false;
        //loops each bit from LSB to most significant then performs bitwise addition
        for(int i = 0; i < maxLen; i++){
            boolean bitA = i < a.length ? a[a.length - i - 1] : false;
            boolean bitB = i < b.length ? b[b.length - i - 1] : false;

            result[maxLen - i] = bitA ^ bitB ^ carry;
            carry = (bitA && bitB) || (bitA && carry) || (bitB && carry);
        }
        result[0] = carry;

        return result;
    }
    private static boolean[] shiftLeft(boolean[] bits, int spot){
        //create a new array big enough to cover the original bits
        boolean[] shift = new boolean[bits.length + spot];
        //empty positions are padded false
        System.arraycopy(bits, 0, shift, 0, bits.length);
        return shift;
    }
    public static boolean[] deleteZeros(boolean[] bits){
        //checks the index of the first binary 1 in the array
        int leader = 0;
        //increments until it reaches the first 1
        while(leader < bits.length && !bits[leader]){
            leader++;
        }
        return Arrays.copyOfRange(bits, leader, bits.length);
    }
}
