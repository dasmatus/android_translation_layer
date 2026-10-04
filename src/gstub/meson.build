srcs = [
	'com/google/android/gms/providerinstaller/ProviderInstallerImpl.java',
	# Minimal Play services Task<T>, enough for the "request().addOnSuccessListener()" pattern.
	'com/google/android/gms/tasks/Task.java',
	'com/google/android/gms/tasks/Tasks.java',
	'com/google/android/gms/tasks/ImmediateTask.java',
	'com/google/android/gms/tasks/OnSuccessListener.java',
	'com/google/android/gms/tasks/OnFailureListener.java',
	'com/google/android/gms/tasks/OnCompleteListener.java',
	'com/google/android/gms/tasks/OnCanceledListener.java',
	'com/google/android/gms/tasks/RuntimeExecutionException.java',
	# Play services availability: report the locally-served subset as present.
	'com/google/android/gms/common/ConnectionResult.java',
	'com/google/android/gms/common/GoogleApiAvailabilityLight.java',
	'com/google/android/gms/common/GoogleApiAvailability.java',
	'com/google/android/gms/common/GooglePlayServicesUtil.java',
	# Play Integrity API: software-unverified verdicts. See README.md for limits.
	'com/google/android/play/core/integrity/SoftwareVerdict.java',
	'com/google/android/play/core/integrity/IntegrityManager.java',
	'com/google/android/play/core/integrity/IntegrityManagerFactory.java',
	'com/google/android/play/core/integrity/IntegrityTokenRequest.java',
	'com/google/android/play/core/integrity/IntegrityTokenResponse.java',
	'com/google/android/play/core/integrity/StandardIntegrityManager.java',
	'com/google/android/play/core/integrity/IntegrityServiceException.java',
]
java_args = [
    '-bootclasspath', bootclasspath,
    '-source', '1.8', '-target', '1.8',
    '-encoding', 'UTF-8',
    '-Xlint:-deprecation', # we implement deprecated APIs
    '-h', join_paths(dir_base, 'src/api-impl-jni/generated_headers')
]

ant = find_program('ant', required: false)

if ant.found()
    gstub_jar = custom_target('gstub',
                              output: 'gstub.jar',
                              command: [ant,
                                        '-buildfile', files('build.xml'),
                                        '-Dbasedir=' + builddir_base,
                                        '-Dsrcdir=' + meson.current_source_dir(),
                                        '-Dincludes=' + ','.join(srcs),
                                        '-Djarfile=@OUTPUT@',
                                        '-Dbuild=@PRIVATE_DIR@',
                                        '-Djavaargs=' + ' '.join(java_args + ['-classpath', hax_jar.full_path()]),
                              ],
                              depends: [hax_jar],
                              depend_files: srcs + ['build.xml'],
    )
else
    gstub_jar = jar('gstub', srcs,
                    link_with: [hax_jar],
                    java_args: java_args,)
endif
