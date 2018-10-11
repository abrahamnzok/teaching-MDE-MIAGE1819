/**
 * generated by Xtext 2.15.0
 */
package org.xtext.example.mydsl.videoGen;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optional Image</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.videoGen.OptionalImage#getDescription <em>Description</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.videoGen.VideoGenPackage#getOptionalImage()
 * @model
 * @generated
 */
public interface OptionalImage extends Image
{
  /**
   * Returns the value of the '<em><b>Description</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Description</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' containment reference.
   * @see #setDescription(ImageDescription)
   * @see org.xtext.example.mydsl.videoGen.VideoGenPackage#getOptionalImage_Description()
   * @model containment="true"
   * @generated
   */
  ImageDescription getDescription();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.videoGen.OptionalImage#getDescription <em>Description</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' containment reference.
   * @see #getDescription()
   * @generated
   */
  void setDescription(ImageDescription value);

} // OptionalImage
