/*
 * generated by Xtext 2.15.0
 */
package org.xtext.example.mydsl


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class VideoGenStandaloneSetup extends VideoGenStandaloneSetupGenerated {

	def static void doSetup() {
		new VideoGenStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
