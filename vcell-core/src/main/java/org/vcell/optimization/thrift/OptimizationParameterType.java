/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.optimization.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum OptimizationParameterType implements org.apache.thrift.TEnum {
  Number_of_Generations(0),
  Number_of_Iterations(1),
  Population_Size(2),
  Random_Number_Generator(3),
  Seed(4),
  IterationLimit(5),
  Tolerance(6),
  Rho(7),
  Scale(8),
  Swarm_Size(9),
  Std_Deviation(10),
  Start_Temperature(11),
  Cooling_Factor(12),
  Pf(13);

  private final int value;

  private OptimizationParameterType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static OptimizationParameterType findByValue(int value) { 
    switch (value) {
      case 0:
        return Number_of_Generations;
      case 1:
        return Number_of_Iterations;
      case 2:
        return Population_Size;
      case 3:
        return Random_Number_Generator;
      case 4:
        return Seed;
      case 5:
        return IterationLimit;
      case 6:
        return Tolerance;
      case 7:
        return Rho;
      case 8:
        return Scale;
      case 9:
        return Swarm_Size;
      case 10:
        return Std_Deviation;
      case 11:
        return Start_Temperature;
      case 12:
        return Cooling_Factor;
      case 13:
        return Pf;
      default:
        return null;
    }
  }
}
