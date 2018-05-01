/*
 * issfinder by Daniel Pfeifer (RedBridge Group).
 *
 * To the extent possible under law, the person who associated CC0 with
 * issfinder has waived all copyright and related or neighboring rights
 * to issfinder.
 *
 * You should have received a copy of the CC0 legalcode along with this
 * work.  If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

pipeline {

  agent {
    node {
      label "maven"
    }
  }

  stages {

    // Compile stage
    stage("Run Maven") {
      steps {
        sh 'mvn -s ./settings.xml package'
      }
    }

    stage("Build image") {
      steps {
        sh 'oc start-build issmonolith --from-file=target/monolith-swarm.jar --follow'
      }
    }

    stage("Approval for Prod") {
      steps {
        input message: 'Good to go?', ok: 'Do it!'
      }
    }

    stage("Promote to production") {
      steps {
        sh 'oc tag issmonolith:latest issmonolith-prod:latest'
      }
    }
  }
}
