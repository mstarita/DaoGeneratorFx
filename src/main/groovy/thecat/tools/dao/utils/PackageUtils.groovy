package thecat.tools.dao.utils

/**
 * Created by thecat on 10/10/16.
 */
class PackageUtils {

    static def calculateDaoPackage(fqClassName) {
        def splittedPackage = []
        def daoPackage = null
        def splittedFqClassName = fqClassName.split('[.]')

        splittedFqClassName.eachWithIndex { it, i -> if (i+1 != splittedFqClassName.size()) splittedPackage.add it }

        if (splittedPackage.contains('model')) {
            daoPackage = splittedPackage[0..(splittedPackage.findIndexOf { it == 'model' } - 1)].join('.') + '.dao'
        } else {
            if (splittedPackage.size() > 2) {
                daoPackage = splittedPackage[0..(splittedPackage.size() - 3)].join('.') + '.dao'
            } else {
                daoPackage = splittedPackage.join('.')
            }
        }

        daoPackage
    }

}
